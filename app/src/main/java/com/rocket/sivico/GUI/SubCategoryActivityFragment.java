package com.rocket.sivico.GUI;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rocket.sivico.Adapters.SubCategoryHolder;
import com.rocket.sivico.Data.Category;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.SubCategory;
import com.rocket.sivico.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubCategoryActivityFragment extends Fragment {
    private static final int CREATE_REPORT_REQUEST = 2003;

    private View rootView;
    private DatabaseReference subCatRef = FirebaseDatabase.getInstance().getReference("subcategories");
    private RecyclerView children;
    private LinearLayoutManager layoutManagerSub;
    private FirebaseRecyclerAdapter<SubCategory, SubCategoryHolder> subCatAdapter;

    public SubCategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sub_category, container, false);
        Bundle b = getArguments();
        Category category = b.getParcelable(GlobalConfig.PARAM_CATEGORY);
        children = rootView.findViewById(R.id.child_category_list);
        subCatAdapter = getAdapter(category);
        children.setLayoutManager(new LinearLayoutManager(getContext()));
        layoutManagerSub = new LinearLayoutManager(getContext());
        children.setLayoutManager(layoutManagerSub);

        // Scroll to bottom on new messages
        subCatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                layoutManagerSub.smoothScrollToPosition(children, null, subCatAdapter.getItemCount());
            }
        });

        children.setAdapter(subCatAdapter);
        subCatAdapter.startListening();

        subCatRef.keepSynced(true);
        return rootView;
    }

    private FirebaseRecyclerAdapter<SubCategory, SubCategoryHolder> getAdapter(Category category) {
        Query query = subCatRef.orderByChild("parent").startAt(category.getId()).endAt(category.getId());
        FirebaseRecyclerOptions<SubCategory> options =
                new FirebaseRecyclerOptions.Builder<SubCategory>()
                        .setQuery(query, SubCategory.class)
                        .build();
        return new FirebaseRecyclerAdapter<SubCategory, SubCategoryHolder>(options) {
            @Override
            public SubCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_item_view, parent, false);
                return new SubCategoryHolder(view);
            }


            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
//                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            protected void onBindViewHolder(SubCategoryHolder holder, int position, final SubCategory subCategory) {
                holder.bind(subCategory, position + 1);
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), NewReportActivity.class);
                        intent.putExtra(GlobalConfig.PARAM_SUBCATEGORY, subCategory);
                        startActivityForResult(intent, CREATE_REPORT_REQUEST);
                    }
                });
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subCatAdapter != null) {
            subCatAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (subCatAdapter != null) {
            subCatAdapter.startListening();
        }
    }
}
