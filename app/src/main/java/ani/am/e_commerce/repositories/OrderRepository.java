package ani.am.e_commerce.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

import ani.am.e_commerce.api.ApiInterface;
import ani.am.e_commerce.db.dao.OrderDao;
import ani.am.e_commerce.db.entity.Order;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ani.am.e_commerce.activities.MainActivity.prefConfig;

@Singleton
public class OrderRepository {
    private final ApiInterface apiInterface;
    private final OrderDao orderDao;
    private final Executor executor;

    public OrderRepository(ApiInterface apiInterface, OrderDao orderDao, Executor executor) {
        this.apiInterface = apiInterface;
        this.orderDao = orderDao;
        this.executor = executor;
    }

    public void createOrder(Order order) {
        String token = prefConfig.readToken("token");
        executor.execute(() -> {
            apiInterface.createOrder(token, order).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("Tag", "createOrder " + response);
                    if (response.isSuccessful() && response.body() != null)
                        saveOrder(response.body().toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        });
    }

    public LiveData<List<Order>> gatOrdersBySellerId(String sellerId) {
        refreshOrdersBySellerId();
        return orderDao.getOrdersBySellerId(sellerId);
    }

    public LiveData<List<Order>> gatOrdersByBuyerId(String buyerId) {
        refreshOrdersByBuyerId();
        return orderDao.getOrdersByBuyerId(buyerId);
    }

    public void deleteOrder(Order order){
        String token = prefConfig.readToken("token");
        executor.execute(() -> {
            apiInterface.deleteOrder(token, order.getOrderId()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("Tag", "deleteOrder " + response);
                    if (response.isSuccessful() && response.body() != null)
                        orderDao.deleteOrder(order);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        });
    }

    private void refreshOrdersBySellerId() {
        String token = prefConfig.readToken("token");
        executor.execute(() ->
                apiInterface.getOrdersBySellerId(token).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", "getOrdersBySellerId" + response.toString());
                        if (response.body() != null) {
                            saveOrdersList(response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                }));
    }

    private void refreshOrdersByBuyerId() {
        String token = prefConfig.readToken("token");
        executor.execute(() ->
                apiInterface.getOrdersByBuyerId(token).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", "getOrdersByBuyerId" + response.toString());
                        if (response.body() != null) {
                            saveOrdersList(response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                }));
    }

    private void saveOrdersList(String json) {
        executor.execute(() -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray data = jsonObject.getJSONArray("data");
                List<Order> ordersList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.getJSONObject(i);
                    Order order = gson.fromJson(object.toString(), Order.class);
                    ordersList.add(order);
                }
                orderDao.saveOrdersList(ordersList);
                Log.d("Tag", "saveOrdersList " + ordersList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveOrder(String json) {
        executor.execute(() -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject data = jsonObject.getJSONObject("data");
                Gson gson = new Gson();
                Order order = gson.fromJson(data.toString(), Order.class);
                orderDao.createOrder(order);
                Log.d("Tag", "saveOrder");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

}
