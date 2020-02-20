package ani.am.e_commerce.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.net.URL;
import java.util.List;

import ani.am.e_commerce.R;
import ani.am.e_commerce.activites.ProductsActivity;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categorisList;
    private List<Product> productsList;
    Context context;
    private View categoryView;

    public CategoryAdapter(List<Category> list) {
        categorisList = list;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            image = itemView.findViewById(R.id.category_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String json = gson.toJson(categorisList.get(getAdapterPosition()));
                    Intent intent = new Intent(context, ProductsActivity.class);
                    intent.putExtra("category", json);
                    ((Activity) context).startActivity(intent);
                }
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
            Glide.with(context).load("http://5.9.1.58:3000/" + category.getCategoryPicture())
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return categorisList.size();
    }
}
