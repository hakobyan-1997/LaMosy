package ani.am.e_commerce.activities;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONObject;

import java.math.BigDecimal;

import javax.inject.Inject;

import ani.am.e_commerce.Constants;
import ani.am.e_commerce.R;
import ani.am.e_commerce.db.entity.OrderDetails;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.view_models.OrderViewModel;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import static ani.am.e_commerce.Constants.BASE_URL;
import static ani.am.e_commerce.activities.MainActivity.prefConfig;

public class OrderActivity extends AppCompatActivity implements HasActivityInjector {
    private final static int PAYPAL_REQUEST_CODE = 9999;
    private TextView productQuantity;
    private ImageView praductImage;
    private ImageView incIv;
    private ImageView decIv;
    private EditText countryEt;
    private EditText cityEt;
    private EditText regionEt;
    private EditText addressEt;
    private EditText phoneEt;
    private TextView totalPriceTv;
    private Button btnBuy;
    private int quantity = 1;
    private int totalPrice;
    private int maxQuantity = 5;
    private Product product;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;
    private OrderViewModel orderViewModel;

    //Paypal payment
    public PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constants.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initToolBar();
        AndroidInjection.inject(this);
        orderViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(OrderViewModel.class);
        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null)
            init();

        //init paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    private void init() {
        productQuantity = findViewById(R.id.product_quantity);
        praductImage = findViewById(R.id.product_image);
        incIv = findViewById(R.id.inc_iv);
        decIv = findViewById(R.id.dec_iv);
        totalPriceTv = findViewById(R.id.total_price);
        totalPrice = product.getPrice();
        totalPriceTv.setText(String.valueOf(totalPrice).concat(" $"));
        maxQuantity = product.getCount();
        incIv.setOnClickListener(view -> {
            if (quantity < maxQuantity) {
                quantity++;
                totalPrice = quantity * product.getPrice();
                totalPriceTv.setText(String.valueOf(totalPrice).concat(" $"));
            }
            productQuantity.setText(String.valueOf(quantity));
        });

        decIv.setOnClickListener(view -> {
            if (quantity >= 2) {
                quantity--;
                totalPrice = quantity * product.getPrice();
                totalPriceTv.setText(String.valueOf(totalPrice).concat(" $"));
            }
            productQuantity.setText(String.valueOf(quantity));
        });

        if (product.getPicture() != " ") {
            String path = BASE_URL + "/" + product.getPicture();
            path = path.replace("\\", "/");
            Glide.with(this).load(path)
                    .into(praductImage);
        }
        TextView productNameTv = findViewById(R.id.product_name_tv);
        productNameTv.setText(product.getName());
        btnBuy = findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(view -> showPaymentDialog());
    }

    private void createOrder() {
        String name = product.getName();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(totalPrice)),
                "USD", name,
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetail);
                        String state = jsonObject.getJSONObject("response").getString("state");
                        String sellerId = product.getUserId();
                        if (sellerId == null)
                            sellerId = "5e8996048d34d63a9851319f";
                        String quantity = productQuantity.getText().toString();
                        String total = String.valueOf(totalPrice);
                        String buyerName = prefConfig.readName();
                        String city = cityEt.getText().toString();
                        String region = regionEt.getText().toString();
                        String country = countryEt.getText().toString();
                        String addres = addressEt.getText().toString();
                        String phone = phoneEt.getText().toString();
                        String productId = product.getId();
                        OrderDetails orderDetails = new OrderDetails(productId, Integer.valueOf(quantity), sellerId, city, country, addres, region, phone, buyerName, Integer.valueOf(total));
                        orderViewModel.createOrder(this, orderDetails);

                    } catch (Exception e) {

                    }
                }
            } else if (requestCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, getString(R.string.payment_cancel), Toast.LENGTH_SHORT).show();
            else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
                Toast.makeText(this, getString(R.string.invalid_payment), Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolBar() {
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getString(R.string.order));
    }

    private void showPaymentDialog() {
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_order);
        countryEt = dialog.findViewById(R.id.country);
        cityEt = dialog.findViewById(R.id.city);
        regionEt = dialog.findViewById(R.id.region);
        addressEt = dialog.findViewById(R.id.address);
        phoneEt = dialog.findViewById(R.id.phone);
        TextView cancel = dialog.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(v -> dialog.dismiss());
        TextView ok = dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(v -> {
            createOrder();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
