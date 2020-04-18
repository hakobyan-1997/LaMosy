package ani.am.e_commerce.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.List;

import ani.am.e_commerce.R;
import ani.am.e_commerce.activities.UserProductsActivity;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.interfaces.CustomOnClickListener;

import static ani.am.e_commerce.Constants.BASE_URL;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ViewHolder> {
    private List<Product> productsList;
    private Context context;
    private View view;
    private boolean isUser;
    private ViewHolder viewHolder;
    private CustomOnClickListener customOnClickListener;

    public UserProductAdapter(List<Product> list, CustomOnClickListener customOnClickListener,boolean isUser) {
        this.productsList = list;
        this.isUser = isUser;
        this.customOnClickListener = customOnClickListener;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView price;
        final ImageView image;
        final ImageView delete;
        final ImageView edit;
        final RelativeLayout contentLayout;
        final ImageView stars[] = new ImageView[5];

        public ViewHolder(View itemView) {
            super(itemView);
            contentLayout = itemView.findViewById(R.id.content_layout);
            name = itemView.findViewById(R.id.product_name);
            image = itemView.findViewById(R.id.product_image);
            price = itemView.findViewById(R.id.product_price);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            stars[0] = itemView.findViewById(R.id.star_1);
            stars[1] = itemView.findViewById(R.id.star_2);
            stars[2] = itemView.findViewById(R.id.star_3);
            stars[3] = itemView.findViewById(R.id.star_4);
            stars[4] = itemView.findViewById(R.id.star_5);
            if(isUser) {
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }else{
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    public UserProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_product, parent, false);
        viewHolder = new ViewHolder(view);
        if(isUser) {
            final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shake_infinite);
            viewHolder.delete.setAnimation(animShake);
            viewHolder.edit.setAnimation(animShake);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductAdapter.ViewHolder viewHolder, int position) {
        Product product = productsList.get(position);
        viewHolder.name.setText(product.getName());
        viewHolder.price.setText((String.valueOf(product.getPrice())).concat("$"));
        viewHolder.contentLayout.setOnClickListener(v -> customOnClickListener.onClickListener(position));
        URL url = null;
        Log.d("Tag", product.getPicture());
        if (product.getPicture() != " ") {
            String path = BASE_URL + "/" + product.getPicture();
            path = path.replace("\\", "/");
            Glide.with(context).load(path)
                    .into(viewHolder.image);
        }
        setStarsColorFilter(position);

        if(isUser) {
            viewHolder.delete.setOnClickListener(v -> {
                showDeleteDialog(product, position);
            });
            viewHolder.edit.setOnClickListener(view1 -> {
                customOnClickListener.editProduct(product);
            });
        }
    }

    private void setStarsColorFilter(int position) {
        Product product = productsList.get(position);
        int productStars = product.getStars();
        if (productStars > 5)
            productStars = 5;
        for (int i = 0; i < productStars; i++) {
            viewHolder.stars[i].setColorFilter(ContextCompat.getColor(context, R.color.md_yellow_700), PorterDuff.Mode.SRC_IN);
        }
    }

    private void showDeleteDialog(Product product, int position) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(context.getString(R.string.delete_msg));

        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(product);
                productsList.remove(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void deleteProduct(Product product) {
        ((UserProductsActivity) context).deleteProduct(product);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
