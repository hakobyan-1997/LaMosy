package ani.am.e_commerce.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ani.am.e_commerce.R;
import ani.am.e_commerce.db.entity.Order;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.fragments.OrdersFragment;

import static ani.am.e_commerce.Constants.BASE_URL;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> ordersList;
    private OrdersFragment ordersFragment;
    private boolean isSeller;
    private Context context;

    public OrderAdapter(List<Order> ordersList, boolean isSeller, OrdersFragment ordersFragment) {
        this.ordersList = ordersList;
        this.isSeller = isSeller;
        this.ordersFragment = ordersFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Order order = ordersList.get(position);
        Product product = order.getProduct();
        viewHolder.productNameTv.setText(product.getName());
        viewHolder.quantityTv.setText(String.valueOf(order.getProductQuantity()));
        viewHolder.totalTv.setText(String.valueOf(order.getTotalPrice()));
        String date = order.getDeliveryDate().replace("T", " ");
        date = date.replace("Z", "");
        viewHolder.deliveryDate.setText(date);
        if (product.getPicture() != " ") {
            String path = BASE_URL + "/" + product.getPicture();
            path = path.replace("\\", "/");
            Glide.with(context).load(path)
                    .into(viewHolder.productIv);
        }
        viewHolder.delete.setOnClickListener(view -> {
            ordersFragment.orderViewModel.deleteOrder(order);
            notifyDataSetChanged();
        });

        if (isSeller) {
            viewHolder.adressLayout.setVisibility(View.VISIBLE);
            String address = order.getCountry() + " " + order.getCity() + " " + order.getRegion() + " " + order.getAddress();
            viewHolder.addressTv.setText(address);
        } else
            viewHolder.adressLayout.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout adressLayout;
        final TextView buyerNameTv;
        final TextView buyerPhoneTv;
        final TextView addressTv;
        final TextView totalTv;
        final TextView quantityTv;
        final TextView productNameTv;
        final ImageView productIv;
        final TextView deliveryDate;
        final ImageView delete;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            adressLayout = itemView.findViewById(R.id.address_layout);
            buyerNameTv = itemView.findViewById(R.id.buyer_name);
            buyerPhoneTv = itemView.findViewById(R.id.buyer_phone);
            addressTv = itemView.findViewById(R.id.buyer_address);
            totalTv = itemView.findViewById(R.id.total);
            quantityTv = itemView.findViewById(R.id.quantity);
            productNameTv = itemView.findViewById(R.id.product_name);
            productIv = itemView.findViewById(R.id.product_image);
            deliveryDate = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
