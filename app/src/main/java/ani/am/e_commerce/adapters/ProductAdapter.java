package ani.am.e_commerce.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.List;

import ani.am.e_commerce.R;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.interfaces.CustomOnClickListener;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private CustomOnClickListener clickListener;
    private List<Product> productsList;
    private Context context;
    private View view;
    private ViewHolder viewHolder;

    public ProductAdapter(List<Product> list, CustomOnClickListener clickListener) {
        this.productsList = list;
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final RelativeLayout contentLayout;
        final TextView name;
        final TextView price;
        final ImageView image;
        final RatingBar ratingBar;
        final ImageView stars[] = new ImageView[5];

        ViewHolder(View itemView) {
            super(itemView);
            contentLayout = itemView.findViewById(R.id.content_layout);
            name = itemView.findViewById(R.id.product_name);
            image = itemView.findViewById(R.id.product_image);
            price = itemView.findViewById(R.id.product_price);
            ratingBar = itemView.findViewById(R.id.radioBar);
            stars[0] = itemView.findViewById(R.id.star_1);
            stars[1] = itemView.findViewById(R.id.star_2);
            stars[2] = itemView.findViewById(R.id.star_3);
            stars[3] = itemView.findViewById(R.id.star_4);
            stars[4] = itemView.findViewById(R.id.star_5);
        }
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_product, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder viewHolder, int position) {
        Product product = productsList.get(position);
        viewHolder.name.setText(product.getName());
        viewHolder.price.setText((String.valueOf(product.getPrice())).concat("$"));
        viewHolder.contentLayout.setOnClickListener(v -> clickListener.onClickListener(position));
        URL url = null;
        Log.d("Tag", product.getPicture());
        if (product.getPicture() != " ") {
            Glide.with(context).load("http://5.9.1.58:3000/" + product.getPicture())
                    .into(viewHolder.image);
        }
        int productStars = product.getStars();
        if (productStars > 5)
            productStars = 5;
        /*viewHolder.ratingBar.setRating(productStars);*/
        setStarsColorFilter(productStars);
    }

    private void setStarsColorFilter(int productStars) {
        for (int i = 0; i < productStars; i++) {
            viewHolder.stars[i].setColorFilter(ContextCompat.getColor(context, R.color.md_yellow_700), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
