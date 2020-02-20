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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.List;

import ani.am.e_commerce.R;
import ani.am.e_commerce.activites.UserProductsActivity;
import ani.am.e_commerce.db.entity.Product;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ViewHolder> {
    private List<Product> productsList;
    Context context;
    private View view;
    private ViewHolder viewHolder;

    public UserProductAdapter(List<Product> list) {
        productsList = list;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView description;
        final TextView price;
        final ImageView image;
        final ImageView delete;
        final ImageView stars[] = new ImageView[5];

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            image = itemView.findViewById(R.id.product_image);
            description = itemView.findViewById(R.id.product_short_description);
            price = itemView.findViewById(R.id.product_price);
            delete = itemView.findViewById(R.id.delete);
            stars[0] = itemView.findViewById(R.id.star_1);
            stars[1] = itemView.findViewById(R.id.star_2);
            stars[2] = itemView.findViewById(R.id.star_3);
            stars[3] = itemView.findViewById(R.id.star_4);
            stars[4] = itemView.findViewById(R.id.star_5);
            delete.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public UserProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_product, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductAdapter.ViewHolder viewHolder, int position) {
        Product product = productsList.get(position);
        TextView textView = viewHolder.name;
        textView.setText(product.getName());
        viewHolder.description.setText(product.getDescription());
        viewHolder.price.setText(context.getString(R.string.price).concat(" ").concat(String.valueOf(product.getPrice())).concat( "$"));
        URL url = null;
        Log.d("Tag", product.getPicture());
        if (product.getPicture() != " ") {
            Glide.with(context).load("http://5.9.1.58:3000/" + product.getPicture())
                    .into(viewHolder.image);
        }
        setStarsColorFilter(position);

        viewHolder.delete.setOnClickListener(v -> deleteProdut(product));
    }

    private void setStarsColorFilter(int position) {
        Product product = productsList.get(position);
        int productStars = product.getStars();
        if (productStars > 5)
            productStars = 5;
        for (int i = 0; i < productStars; i++) {
            viewHolder.stars[i].setColorFilter(ContextCompat.getColor(context,R.color.md_yellow_700), PorterDuff.Mode.SRC_IN);
        }
    }

    private void deleteProdut(Product product){
        ((UserProductsActivity)context).deleteProduct(product);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
