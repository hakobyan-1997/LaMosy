package ani.am.e_commerce.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import ani.am.e_commerce.R;
import ani.am.e_commerce.db.entity.Order;
import ani.am.e_commerce.db.entity.Product;

import static ani.am.e_commerce.Constants.BASE_URL;
import static ani.am.e_commerce.activities.MainActivity.prefConfig;

public class OrderActivity extends AppCompatActivity {
    private TextView productQuantity;
    private ImageView praductImage;
    private ImageView incIv;
    private ImageView decIv;
    private EditText countryEt;
    private EditText cityEt;
    private EditText regionEt;
    private EditText addressEt;
    private EditText phoneEt;
    private Button btnBuy;
    private int quantity = 1;
    private int maxQuantity = 5;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initToolBar();
        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null)
            init();
    }

    private void init() {
        productQuantity = findViewById(R.id.product_quantity);
        praductImage = findViewById(R.id.product_image);
        incIv = findViewById(R.id.inc_iv);
        decIv = findViewById(R.id.dec_iv);
        countryEt = findViewById(R.id.country);
        cityEt = findViewById(R.id.city);
        regionEt = findViewById(R.id.region);
        addressEt = findViewById(R.id.address);
        phoneEt = findViewById(R.id.phone);

        incIv.setOnClickListener(view -> {
            if (quantity < maxQuantity)
                quantity++;
            productQuantity.setText(String.valueOf(quantity));
        });

        decIv.setOnClickListener(view -> {
            if (quantity >= 2)
                quantity--;
            productQuantity.setText(String.valueOf(quantity));
        });

        if (product.getPicture() != " ") {
            String path = BASE_URL + "/" + product.getPicture();
            path = path.replace("\\", "/");
            Glide.with(this).load(path)
                    .into(praductImage);
        }
        btnBuy = findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(view -> createOrder());
    }

    private void createOrder() {
        Log.d("Tag","order product " + product );
    }

    private void initToolBar() {
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getString(R.string.order));
    }
}
