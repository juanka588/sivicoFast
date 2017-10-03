package com.rocket.sivico.GUI;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocket.sivico.Adapters.CategoryAdapter;
import com.rocket.sivico.Data.Category;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Interfaces.OnCategoryClick;
import com.rocket.sivico.R;

import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

/**
 * A placeholder fragment containing a simple view.
 */
public class CategoryActivityFragment extends Fragment implements OnCategoryClick {

    private List<Category> mainCats;
    private RecyclerView children;

    public CategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainCats = GlobalConfig.initCategories();
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        RecyclerView mainCategoryList = view.findViewById(R.id.main_category_list);
        mainCategoryList.setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));
        mainCategoryList.setAdapter(new CategoryAdapter(R.layout.category_main_item_view, mainCats, this));

        children = view.findViewById(R.id.child_category_list);
        children.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onCategoryClick(Category category) {
        if (category.getParent() == null) {
            children.setAdapter(new CategoryAdapter(R.layout.category_item_view, category.getChildren(), this));
        } else {
            Intent intent = new Intent(getActivity(), NewReportActivity.class);
            intent.putExtra(GlobalConfig.PARAM_CATEGORY, category);
            startActivity(intent);
        }
    }
}
