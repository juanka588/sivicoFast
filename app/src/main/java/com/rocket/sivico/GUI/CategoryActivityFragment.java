package com.rocket.sivico.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

    private View rootView;
    private DatabaseReference catRef = FirebaseDatabase.getInstance().getReference("categories");
    private RecyclerView mainCategoryList;
    private LinearLayoutManager layoutManagerMain;
    private FirebaseRecyclerAdapter<Category, CategoryHolder> mainCatAdapter;


    public CategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        mainCatAdapter = getAdapter();
        mainCatAdapter.startListening();

        mainCategoryList = rootView.findViewById(R.id.main_category_list);
        layoutManagerMain = new GridLayoutManager(getContext(), 2);
        mainCategoryList.setLayoutManager(layoutManagerMain);
        mainCategoryList.setAdapter(mainCatAdapter);

        catRef.keepSynced(true);
        return rootView;
    }

    @Override
    public void onCategoryClick(Category category) {
        Log.e(TAG, category.getName() + " id: " + category.getId());
        Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
        intent.putExtra(GlobalConfig.PARAM_CATEGORY, category);
        startActivity(intent);
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
                        .inflate(R.layout.category_main_2_item_view, parent, false);
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
                if (holder.next == null) {
                    return;
                }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainCatAdapter != null) {
            mainCatAdapter.startListening();
        }
    }
}
