package ani.am.e_commerce.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.yayandroid.parallaxrecyclerview.ParallaxImageView;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import java.net.URL;
import java.util.List;

import ani.am.e_commerce.R;
import ani.am.e_commerce.activities.UserProductsActivity;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.interfaces.CustomOnClickListener;

import static ani.am.e_commerce.Constants.BASE_URL;

public class UserCategoryAdapter extends ParallaxRecyclerView.Adapter<UserCategoryAdapter.ViewHolder> {
    private List<Category> categoriesList;
    private Context context;
    private CustomOnClickListener customOnClickListener;
    private boolean isUser;

    public UserCategoryAdapter(List<Category> list, CustomOnClickListener customOnClickListener, boolean isUser) {
        this.isUser = isUser;
        this.categoriesList = list;
        this.customOnClickListener = customOnClickListener;
    }

    protected class ViewHolder extends ParallaxViewHolder {
        final TextView name;
        final ParallaxImageView image;
        final ImageView delete;
        final ImageView edit;

        @Override
        public int getParallaxImageId() {
            return R.id.category_image;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            image = itemView.findViewById(R.id.category_image);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, UserProductsActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(categoriesList.get(getAdapterPosition()));
                intent.putExtra("category", json);
                intent.putExtra("isUser", isUser);
                ((Activity) context).startActivity(intent);
            });
            if (isUser) {
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    public UserCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.item_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(categoryView);
        if (isUser) {
            final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shake_infinite);
            viewHolder.delete.setAnimation(animShake);
            viewHolder.edit.setAnimation(animShake);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCategoryAdapter.ViewHolder viewHolder, int position) {
        Category category = categoriesList.get(position);
        TextView textView = viewHolder.name;
        textView.setText(category.getCategoryName());
        ImageView imageView = viewHolder.image;
        URL url = null;
        Log.d("tet", "category " + category);
        if (category.getCategoryPicture() != " ") {
            String path = BASE_URL + "/" + category.getCategoryPicture();
            path = path.replace("\\", "/");
            Glide.with(context).load(path)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageView);
        }
        if (isUser) {
            viewHolder.delete.setOnClickListener(v -> {
                showDeleteDialog(position);
            });
            viewHolder.edit.setOnClickListener(view1 -> {
                customOnClickListener.editCategory(position);
            });
        }
        viewHolder.getBackgroundImage().reuse();
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    private void showDeleteDialog(final int position) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(context.getString(R.string.delete_msg));

        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());

        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(v -> {
            customOnClickListener.removeCategory(position);
            categoriesList.remove(position);
            dialog.dismiss();
        });
        dialog.show();
    }
}
