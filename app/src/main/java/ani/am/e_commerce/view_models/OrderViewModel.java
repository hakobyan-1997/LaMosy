package ani.am.e_commerce.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Order;
import ani.am.e_commerce.repositories.CategoryRepository;
import ani.am.e_commerce.repositories.OrderRepository;
import okhttp3.RequestBody;

public class OrderViewModel extends ViewModel {
    private OrderRepository orderRepo;

    @Inject
    public OrderViewModel(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public LiveData<List<Order>> getOrdersListBySellerId(String sellerId) {
        return orderRepo.gatOrdersBySellerId(sellerId);
    }

    public LiveData<List<Order>> getOrdersListByBuyerId(String buyerId) {
        return orderRepo.gatOrdersByBuyerId(buyerId);
    }

    public void deleteOrder(Order order) {
        orderRepo.deleteOrder(order);
    }

    public void createOrder(Order order) {
        orderRepo.createOrder(order);
    }
}
