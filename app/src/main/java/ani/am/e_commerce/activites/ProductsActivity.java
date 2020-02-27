package ani.am.e_commerce.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.gson.Gson;

import java.util.List;

import ani.am.e_commerce.PrefConfig;
import ani.am.e_commerce.R;
import ani.am.e_commerce.adapters.ProductAdapter;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;

public class ProductsActivity extends AppCompatActivity {
    private List<Product> productslist;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().setTitle(R.string.products);
        String json = getIntent().getStringExtra("category");

        Gson gson = new Gson();
        category = gson.fromJson(json, Category.class);

        productslist = category.getProducts();
        Log.d("Tag","productsList");
        if (productslist.isEmpty())
            findViewById(R.id.empty_string).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.empty_string).setVisibility(View.GONE);
        RecyclerView productsRv = findViewById(R.id.productsRv);
        runAnimation(productsRv);
    }

    private void runAnimation(RecyclerView recyclerView) {
        ProductAdapter adapter = new ProductAdapter(productslist);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
        recyclerView.setLayoutAnimation(animation);
    }
}
