package ani.am.e_commerce.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yayandroid.parallaxrecyclerview.ParallaxImageView;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import java.net.URL;
import java.util.List;

import ani.am.e_commerce.R;
import ani.am.e_commerce.activities.ProductsActivity;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;

public class CategoryAdapter extends ParallaxRecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categorisList;
    private List<Product> productsList;
    Context context;
    private View categoryView;

    public CategoryAdapter(List<Category> list) {
        categorisList = list;
    }

    protected class ViewHolder extends ParallaxViewHolder {
        public TextView name;
        public ParallaxImageView image;

        @Override
        public int getParallaxImageId() {
            return R.id.category_image;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            image = itemView.findViewById(R.id.category_image);
            itemView.setOnClickListener(v -> {
                Gson gson = new Gson();
                String json = gson.toJson(categorisList.get(getAdapterPosition()));
                Intent intent = new Intent(context, ProductsActivity.class);
                intent.putExtra("category", json);
                ((Activity) context).startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        categoryView = inflater.inflate(R.layout.item_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(categoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder viewHolder, int position) {
        Category category = categorisList.get(position);
        TextView textView = viewHolder.name;
        textView.setText(category.getCategoryName());
        ImageView imageView = viewHolder.image;
        URL url = null;
        Log.d("Tag", category.getCategoryPicture());
        if (category.getCategoryPicture() != " ") {
            Glide.with(context)
                    .load("http://5.9.1.58:3000/" + category.getCategoryPicture())
                    .into(imageView);
        }
        viewHolder.getBackgroundImage().reuse();
    }

    @Override
    public int getItemCount() {
        return categorisList.size();
    }
}
