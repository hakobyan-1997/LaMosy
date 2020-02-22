package ani.am.e_commerce.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;
import ani.am.e_commerce.R;
import ani.am.e_commerce.adapters.UserCategoryAdapter;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.view_models.CategoryViewModel;
import ani.am.e_commerce.view_models.UserViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ProfileFragment extends Fragment {
    private View view;
    private Context context;
    private UserCategoryAdapter adapter;
    List<Category> categoryList;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CategoryViewModel viewModel;
    private UserViewModel userViewModel;

    @BindView(R.id.category_rv)
    RecyclerView categoryRv;

    public ProfileFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewModel();
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel.class);
        userViewModel = ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
        userViewModel.initSession(context);
        getCategoryList();
    }

    private void getCategoryList() {
        viewModel.getUserCategoriesList().observe(this, categories -> updateUI(categories));
    }


    private void updateUI(@Nullable List<Category> categoryList) {
        Log.d("Tag", "update caegories " + categoryList);
        if (categoryList != null) {
            this.categoryList = categoryList;
            createArrayList(categoryList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle(getString(R.string.my_profile));
        ButterKnife.bind(this, view);
        //  getCategories();
        return view;
    }

    private void createArrayList(List<Category> list) {
        Log.d("Tag", "userCategory " + list.toString());
        adapter = new UserCategoryAdapter(list);
        categoryRv.setAdapter(adapter);
        categoryRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                editCategory(item.getGroupId());
                return true;
            case 2:
                removeCategory(item.getGroupId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editCategory(int position) {
        Gson gson = new Gson();
        String json = gson.toJson(categoryList.get(position));
        Fragment fragment = new EditCategoryFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ((EditCategoryFragment) fragment).newInstance(json))
                .addToBackStack(null).commit();
    }

    private void removeCategory(int position) {
        Log.d("Tag", "position " + position);
        Log.d("Tag", "categoryList size " + categoryList.size());
        Category category = categoryList.get(position);
        viewModel.deleteCategory(category);
        getCategoryList();
    }
}
