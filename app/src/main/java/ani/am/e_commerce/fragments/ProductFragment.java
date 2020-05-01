package ani.am.e_commerce.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ani.am.e_commerce.R;
import ani.am.e_commerce.activities.MainActivity;
import ani.am.e_commerce.activities.OrderActivity;
import ani.am.e_commerce.db.entity.Product;

import static ani.am.e_commerce.Constants.BASE_URL;
import static ani.am.e_commerce.activities.MainActivity.prefConfig;

public class ProductFragment extends Fragment {
    private View view;
    private Context context;
    private Product product;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static ProductFragment newInstance(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product, container, false);
        if (getArguments() != null) {
            this.product = (Product) getArguments().getSerializable("product");
            init();
        }
        return view;
    }

    private void init() {
        final Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(product.getName());

        if (product.getDescription() != null) {
            TextView descTv = view.findViewById(R.id.description_tv);
            descTv.setText(product.getDescription());
        }
        if (product.getPicture() != null) {
            ImageView prudImgView = view.findViewById(R.id.product_image_view);
            String path = BASE_URL + "/" + product.getPicture();
            path = path.replace("\\", "/");
            Glide.with(context)
                    .load(path)
                    .into(prudImgView);
        }
        TextView priceTv = view.findViewById(R.id.price_tv);
        String priceStr = context.getString(R.string.price) + " " + product.getPrice() + " $";
        priceTv.setText(priceStr);

        FloatingActionButton buy = view.findViewById(R.id.btnBuy);
        buy.setOnClickListener(view1 -> {
            if (prefConfig.readLoginStatus()) {
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            }else{
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("href", "login");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

}
