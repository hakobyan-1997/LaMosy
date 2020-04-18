package ani.am.e_commerce.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.List;

import javax.inject.Inject;

import ani.am.e_commerce.Global;
import ani.am.e_commerce.R;
import ani.am.e_commerce.adapters.OrderAdapter;
import ani.am.e_commerce.adapters.UserCategoryAdapter;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Order;
import ani.am.e_commerce.view_models.CategoryViewModel;
import ani.am.e_commerce.view_models.OrderViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static ani.am.e_commerce.activities.MainActivity.prefConfig;

public class OrdersFragment extends Fragment {
    private boolean isSeller;
    private boolean isAnimated;
    private Context context;
    private List<Order> orderList;
    private OrderViewModel orderViewModel;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.orders_rv)
    RecyclerView ordersRv;

    public static OrdersFragment newInstance(boolean isSeller) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSeller", isSeller);
        OrdersFragment ordersFragment = new OrdersFragment();
        ordersFragment.setArguments(bundle);
        return ordersFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        Bundle bundle = getArguments();
        if (bundle != null)
            isSeller = bundle.getBoolean("isSeller");
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("Tag", "onActivityCreated ");
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewModel();
        String id = prefConfig.readToken("id");
        if (isSeller) {
            getActivity().setTitle(getString(R.string.orders));
            getOrdersByUserId(id);
        } else {
            getActivity().setTitle(getString(R.string.my_shopping_history));
            getShoppingHistoryByUserId(id);
        }
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        orderViewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderViewModel.class);
    }

    private void getShoppingHistoryByUserId(String id) {
        isAnimated = false;
        Log.d("Tag","getShoppingHistory id " + id);
        orderViewModel.getOrdersListByBuyerId(id).observe(this, orders -> updateUI(orders));
    }

    private void getOrdersByUserId(String id) {
        isAnimated = false;
        orderViewModel.getOrdersListBySellerId(id).observe(this, orders -> updateUI(orders));
    }

    private void updateUI(@Nullable List<Order> orderList) {
        Log.d("Tag", "orders list  update " + orderList.size());
        if (orderList != null) {
            createArrayList(orderList);
            this.orderList = orderList;
        }
    }

    private void createArrayList(List<Order> list) {
        OrderAdapter adapter = new OrderAdapter(list,isSeller);
        ordersRv.setAdapter(adapter);
        ordersRv.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        if (!isAnimated) {
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
            ordersRv.setLayoutAnimation(animation);
            isAnimated = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
