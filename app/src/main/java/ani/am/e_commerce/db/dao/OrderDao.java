package ani.am.e_commerce.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ani.am.e_commerce.db.entity.Order;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface OrderDao {
    @Insert(onConflict = REPLACE)
    void createOrder(Order order);

    @Insert(onConflict = REPLACE)
    void saveOrdersList(List<Order> ordersList);

    @Delete
    void deleteOrder(Order order);

    @Query("SELECT * FROM `Order` WHERE sellerId = :sellerId")
    LiveData<List<Order>> getOrdersBySellerId(String sellerId);

    @Query("SELECT * FROM `Order` WHERE buyerId = :buyerId")
    LiveData<List<Order>> getOrdersByBuyerId(String buyerId);
}
