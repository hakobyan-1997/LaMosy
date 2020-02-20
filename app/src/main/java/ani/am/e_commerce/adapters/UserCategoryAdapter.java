package ani.am.e_commerce.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
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
import ani.am.e_commerce.activites.UserProductsActivity;
import ani.am.e_commerce.db.entity.Category;

public class UserCategoryAdapter extends  RecyclerView.Adapter<UserCategoryAdapter.ViewHolder> {
    private  List<Category> categorisList;
    Context context;
    public UserCategoryAdapter(List<Category> list) {
        categorisList = list;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView name;
        public ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.category_name);
            image = itemView.findViewById(R.id.category_image);
            image.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(v -> {
                Gson gson = new Gson();
                String json = gson.toJson(categorisList.get(getAdapterPosition()));
                Intent intent = new Intent(context, UserProductsActivity.class);
                intent.putExtra("category", json);
                ((Activity) context).startActivity(intent);
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),1,0, context.getString(R.string.edit));
            menu.add(this.getAdapterPosition(),2,0, context.getString(R.string.delete));
        }
    }
    @NonNull
    @Override
    public UserCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.item_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(categoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCategoryAdapter.ViewHolder viewHolder, int position) {
        Category category = categorisList.get(position);
        TextView textView = viewHolder.name;
        textView.setText(category.getCategoryName());
        ImageView imageView = viewHolder.image;
        URL url = null;
        if(category.getCategoryPicture()!=" " ) {
            Glide.with(context).load("http://5.9.1.58:3000/" + category.getCategoryPicture())
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return categorisList.size();
    }
}
