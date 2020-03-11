package ani.am.e_commerce.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import ani.am.e_commerce.Global;
import ani.am.e_commerce.PrefConfig;
import ani.am.e_commerce.R;
import ani.am.e_commerce.adapters.ProductAdapter;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.fragments.ProductFragment;
import ani.am.e_commerce.interfaces.CustomOnClickListener;

import static ani.am.e_commerce.activites.MainActivity.prefConfig;

public class ProductsActivity extends AppCompatActivity implements CustomOnClickListener {
    private List<Product> productslist;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.setLocaleLanguage(this, prefConfig.getLang());
        setContentView(R.layout.activity_products);
        String json = getIntent().getStringExtra("category");
        Gson gson = new Gson();
        category = gson.fromJson(json, Category.class);
        initToolBar();
        productslist = category.getProducts();
        Log.d("Tag", "productsList");
        if (productslist.isEmpty())
            findViewById(R.id.empty_string).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.empty_string).setVisibility(View.GONE);
        RecyclerView productsRv = findViewById(R.id.productsRv);
        runAnimation(productsRv);
    }

    private void initToolBar(){
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(category.getCategoryName());
        if (category.getCategoryPicture() != null) {
            ImageView prudImgView = findViewById(R.id.category_image_view);
            Glide.with(this)
                    .load("http://5.9.1.58:3000/" + category.getCategoryPicture())
                    .into(prudImgView);
        }
    }

    private void runAnimation(RecyclerView recyclerView) {
        ProductAdapter adapter = new ProductAdapter(productslist, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
        recyclerView.setLayoutAnimation(animation);
    }

    @Override
    public void onClickListener(int position) {
        if (productslist == null)
            return;
        Product product = productslist.get(position);
        if (product == null)
            return;
        ProductFragment productFragment = ProductFragment.newInstance(product);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, productFragment)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }*/
}
