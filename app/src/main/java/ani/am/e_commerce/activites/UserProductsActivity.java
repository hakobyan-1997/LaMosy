package ani.am.e_commerce.activites;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import ani.am.e_commerce.PrefConfig;
import ani.am.e_commerce.R;
import ani.am.e_commerce.adapters.UserProductAdapter;
import ani.am.e_commerce.fragments.AddProductFragment;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.view_models.CategoryViewModel;
import ani.am.e_commerce.view_models.ProductViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class UserProductsActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private PrefConfig prefConfig;
    private UserProductAdapter adapter;
    private Category category;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ProductViewModel productViewModel;
    @BindView(R.id.productsRv)
    RecyclerView productsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_products);
        AndroidInjection.inject(this);
        productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel.class);
        ButterKnife.bind(this);
        prefConfig = new PrefConfig(this);
        FloatingActionButton addProductButton = (FloatingActionButton) findViewById(R.id.add_product);
        addProductButton.setOnClickListener(v -> {
            addProductButton.hide();
            openAddProductFragment();
        });

        getSupportActionBar().setTitle(R.string.products);
        String categoryJson = getIntent().getStringExtra("category");
        Log.d("Tag", "categoryJson " + categoryJson);
        Gson gson = new Gson();
        category = gson.fromJson(categoryJson, Category.class);
        Log.d("Tag", "Catgory " + category);
        List<Product> products = category.getProducts();
        if (products.isEmpty())
            findViewById(R.id.empty_string).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.empty_string).setVisibility(View.GONE);

        adapter = new UserProductAdapter(products);
        productsRv.setAdapter(adapter);
        productsRv.setLayoutManager(new LinearLayoutManager(this));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
        productsRv.setLayoutAnimation(animation);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private List<Product> getProductsList(String categoryId) {
        List<Category> categorieList = prefConfig.getCategoriesList("userCategory");
        Log.d("Tag", "categoryList " + categorieList.toString());
        for (Category category : categorieList) {
            if (category.getId().equals(categoryId)) {
                return category.getProducts();
            }
        }
        return null;
    }

    private void openAddProductFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        Fragment fragment = AddProductFragment.newInstance(category.getId());
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public void deleteProduct(Product product) {
        productViewModel.deleteProduct(product);
        adapter.notifyDataSetChanged();
    }
}
