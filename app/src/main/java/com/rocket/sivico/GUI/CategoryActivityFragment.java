package com.rocket.sivico.GUI;

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

        mainCategoryList = rootView.findViewById(R.id.main_category_list);
        layoutManagerMain = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mainCategoryList.setLayoutManager(layoutManagerMain);
        mainCategoryList.setAdapter(mainCatAdapter);
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
    }

    private FirebaseRecyclerAdapter<SubCategory, SubCategoryHolder> getAdapter(Category category) {
        Query query = subCatRef.orderByChild("parent").startAt(category.getId()).endAt(category.getId());
        return new FirebaseRecyclerAdapter<SubCategory, SubCategoryHolder>(
                SubCategory.class,
                R.layout.category_item_view,
                SubCategoryHolder.class,
                query,
                this) {
            @Override
            public void populateViewHolder(SubCategoryHolder holder, final SubCategory subCategory, int position) {
                holder.bind(subCategory, position + 1);
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), NewReportActivity.class);
                        intent.putExtra(GlobalConfig.PARAM_CATEGORY, subCategory);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
//                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }


    public FirebaseRecyclerAdapter<Category, CategoryHolder> getAdapter() {
        Query query = catRef.orderByKey();
        return new FirebaseRecyclerAdapter<Category, CategoryHolder>(
                Category.class,
                R.layout.category_main_item_view,
                CategoryHolder.class,
                query,
                this) {
            @Override
            public void populateViewHolder(CategoryHolder holder, final Category category, int position) {
                holder.bind(category);
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CategoryActivityFragment.this.onCategoryClick(category);
                    }
                });
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
//                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }
}
