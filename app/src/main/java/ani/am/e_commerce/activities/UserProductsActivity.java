package ani.am.e_commerce.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import ani.am.e_commerce.Global;
import ani.am.e_commerce.PrefConfig;
import ani.am.e_commerce.R;
import ani.am.e_commerce.adapters.UserProductAdapter;
import ani.am.e_commerce.fragments.AddProductFragment;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.fragments.ProductFragment;
import ani.am.e_commerce.interfaces.CustomOnClickListener;
import ani.am.e_commerce.view_models.ProductViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static ani.am.e_commerce.Constants.BASE_URL;

public class UserProductsActivity extends AppCompatActivity implements HasSupportFragmentInjector, CustomOnClickListener {
    private PrefConfig prefConfig;
    private UserProductAdapter adapter;
    private Category category;
    private List<Product> productsList;
    private FloatingActionButton addProductButton;
    private boolean isUser;
    private boolean isAnimated;
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
        prefConfig = new PrefConfig(this);
        Global.setLocaleLanguage(this, prefConfig.getLang());
        AndroidInjection.inject(this);
        productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel.class);
        ButterKnife.bind(this);
        init();
        initToolBar();
    }

    private void init() {
        String json = getIntent().getStringExtra("category");
        Gson gson = new Gson();
        category = gson.fromJson(json, Category.class);
        isUser = getIntent().getBooleanExtra("isUser", true);
        addProductButton = (FloatingActionButton) findViewById(R.id.add_product);
        if (isUser) {
            showFloatingActionButton(addProductButton);
            addProductButton.setOnClickListener(v -> {
                hideFloatingActionButton(addProductButton);
                openAddProductFragment();
            });
        } else {
            hideFloatingActionButton(addProductButton);
        }
    }

    private void hideFloatingActionButton(FloatingActionButton fab) {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.setAutoHideEnabled(false);
        }

        fab.hide();
    }

    private void showFloatingActionButton(FloatingActionButton fab) {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.setAutoHideEnabled(true);
        }
        fab.show();
    }

    private void initToolBar() {
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(category.getCategoryName());
        if (category.getCategoryPicture() != null) {
            ImageView prudImgView = findViewById(R.id.category_image_view);
            String path = BASE_URL + "/" + category.getCategoryPicture();
            path = path.replace("\\", "/");
            Glide.with(this)
                    .load(path)
                    .into(prudImgView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Tag", "onResume " + isUser);
        isAnimated = false;
        productViewModel.allProductsListByCategoryId(category.getId()).observe(this, products -> {
            Log.d("Tag", "products " + products);
            if (products != null) {
                initProductsRv(products);
            }
        });
    }

    private void initProductsRv(List<Product> products) {
        if (products.isEmpty())
            findViewById(R.id.empty_string).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.empty_string).setVisibility(View.GONE);
        productsList = products;
        adapter = new UserProductAdapter(products, this, isUser);
        productsRv.setAdapter(adapter);
        productsRv.setLayoutManager(new LinearLayoutManager(this));
        if (!isAnimated) {
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
            productsRv.setLayoutAnimation(animation);
            isAnimated = true;
        }
    }

    private List<Product> getProductsList(String categoryId) {
        List<Category> categoriesList = prefConfig.getCategoriesList("userCategory");
        Log.d("Tag", "categoryList " + categoriesList.toString());
        for (Category category : categoriesList) {
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

    public void openEditFragment(Product product) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        Fragment fragment = AddProductFragment.newInstance(product);
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

    @Override
    public void onClickListener(int position) {
        if (productsList == null)
            return;
        hideFloatingActionButton(addProductButton);
        Product product = productsList.get(position);
        if (product == null)
            return;
        ProductFragment productFragment = ProductFragment.newInstance(product);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, productFragment)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                .commit();
    }

    @Override
    public void editProduct(Product product) {
        openEditFragment(product);
    }

    @Override
    public void editCategory(int position) {

    }

    @Override
    public void removeCategory(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isUser)
            showFloatingActionButton(addProductButton);
        else
            hideFloatingActionButton(addProductButton);
    }
}
