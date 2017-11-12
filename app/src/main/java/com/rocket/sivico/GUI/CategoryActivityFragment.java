package com.rocket.sivico.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rocket.sivico.Adapters.CategoryHolder;
import com.rocket.sivico.Adapters.SubCategoryHolder;
import com.rocket.sivico.Data.Category;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.SubCategory;
import com.rocket.sivico.Interfaces.OnCategoryClick;
import com.rocket.sivico.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CategoryActivityFragment extends Fragment implements OnCategoryClick {

    private static final String TAG = CategoryActivityFragment.class.getSimpleName();
    private static final int CREATE_REPORT_REQUEST = 2003;
    private RecyclerView children;
    private View rootView;
    private DatabaseReference catRef = FirebaseDatabase.getInstance().getReference("categories");
    private DatabaseReference subCatRef = FirebaseDatabase.getInstance().getReference("subcategories");
    private RecyclerView mainCategoryList;
    private LinearLayoutManager layoutManagerMain;
    private LinearLayoutManager layoutManagerSub;
    private FirebaseRecyclerAdapter<Category, CategoryHolder> mainCatAdapter;
    private FirebaseRecyclerAdapter<SubCategory, SubCategoryHolder> subCatAdapter;


    public CategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        mainCatAdapter = getAdapter();
        mainCatAdapter.startListening();

        mainCategoryList = rootView.findViewById(R.id.main_category_list);
        layoutManagerMain = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mainCategoryList.setLayoutManager(layoutManagerMain);
        mainCategoryList.setAdapter(mainCatAdapter);

        catRef.keepSynced(true);
        subCatRef.keepSynced(true);
        return rootView;
    }

    @Override
    public void onCategoryClick(Category category) {
        Log.e(TAG, category.getName() + " id: " + category.getId());
        children = rootView.findViewById(R.id.child_category_list);
        subCatAdapter = getAdapter(category);
        children.setLayoutManager(new LinearLayoutManager(getContext()));
        layoutManagerSub = new LinearLayoutManager(getContext());
        children.setLayoutManager(layoutManagerSub);

        // Scroll to bottom on new messages
        subCatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                layoutManagerSub.smoothScrollToPosition(mainCategoryList, null, subCatAdapter.getItemCount());
            }
        });

        children.setAdapter(subCatAdapter);
        subCatAdapter.startListening();
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
                        intent.putExtra(GlobalConfig.PARAM_CATEGORY, subCategory);
                        startActivityForResult(intent, CREATE_REPORT_REQUEST);
                    }
                });
            }
        };
    }


    public FirebaseRecyclerAdapter<Category, CategoryHolder> getAdapter() {
        Query query = catRef.orderByKey();
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();
        return new FirebaseRecyclerAdapter<Category, CategoryHolder>(options) {
            @Override
            public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_main_item_view, parent, false);
                return new CategoryHolder(view);
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                Log.e(TAG, "data received");
//                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            protected void onBindViewHolder(CategoryHolder holder, final int position, final Category category) {
                holder.bind(category);
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CategoryActivityFragment.this.onCategoryClick(category);
                    }
                });
                holder.next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layoutManagerMain.smoothScrollToPosition(mainCategoryList, null, position + 1);
                    }
                });
                holder.prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position - 1 < 0) {
                            layoutManagerMain.smoothScrollToPosition(mainCategoryList, null, position);
                        } else {
                            layoutManagerMain.smoothScrollToPosition(mainCategoryList, null, position - 1);
                        }
                    }
                });
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_REPORT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                this.getActivity().finish();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainCatAdapter != null) {
            mainCatAdapter.stopListening();
        }
        if (subCatAdapter != null) {
            subCatAdapter.stopListening();
        }
    }
}
