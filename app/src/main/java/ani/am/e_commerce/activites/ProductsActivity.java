package ani.am.e_commerce.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ani.am.e_commerce.PrefConfig;
import ani.am.e_commerce.R;
import ani.am.e_commerce.adapters.ProductAdapter;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;

public class ProductsActivity extends AppCompatActivity {
    private List<Product> productslist;
    private PrefConfig prefConfig;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        prefConfig = new PrefConfig(this);
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
    }
}
