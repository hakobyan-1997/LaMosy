package ani.am.e_commerce.api;

import com.google.gson.JsonObject;

import java.util.Map;
import ani.am.e_commerce.db.entity.OrderDetails;
import ani.am.e_commerce.db.entity.User;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/auth/login")
    Call<JsonObject> performUserLogin(@Body User user);

    @POST("/auth/register")
    Call<JsonObject> performRegistration(@Body User user);

    @POST("/auth/logout")
    Call<JsonObject> performUserLogOut(@Header("x-access-token") String token);

    @POST("/auth/init-session")
    Call<JsonObject> initSession(@Header("x-access-token") String token);

    @Multipart
    @POST("/product-category")
    Call<JsonObject> createCategory(@Header("x-access-token") String token, @PartMap Map<String, RequestBody> params);

    @GET("/product-category/all")
    Call<JsonObject> getAllCategories();

    @GET("/product-category")
    Call<JsonObject> getCategories(@Header("x-access-token") String token);

    @DELETE("/product-category/{id}")
    Call<JsonObject> deleteCategory(@Header("x-access-token") String token, @Path("id") String id);

    @Multipart
    @PUT("/product-category/{id}")
    Call<JsonObject> updateCategory(@Path("id") String id, @Header("x-access-token") String token, @PartMap Map<String, RequestBody> params);

    @GET("/product-category/search/{criteria}")
    Call<JsonObject> searchCategory(@Path("criteria")String criteria);

    @Multipart
    @POST("/product/{productCategoryId}")
    Call<JsonObject> createProduct(@Path("productCategoryId") String productCategoryId, @Header("x-access-token") String token, @PartMap Map<String, RequestBody> params);

    @GET("/product/{productCategoryId}")
    Call<JsonObject> getAllProductByCategoryId(@Path("productCategoryId") String productCategoryId);

    @DELETE("/product/{id}")
    Call<JsonObject> deleteProduct(@Header("x-access-token") String token, @Path("id") String id);

    @Multipart
    @PUT("/product/{id}")
    Call<JsonObject> updateProduct(@Path("id") String id, @Header("x-access-token") String token, @PartMap Map<String, RequestBody> params);

    @GET("/product/search/{criteria}")
    Call<JsonObject> searchProduct(@Path("criteria")String criteria);

    @POST("/order")
    Call<JsonObject> createOrder(@Header("x-access-token") String token, @Body OrderDetails order);

    @GET("/order/buyers")
    Call<JsonObject> getOrdersByBuyerId(@Header("x-access-token") String token);

    @GET("/order/sellers")
    Call<JsonObject> getOrdersBySellerId(@Header("x-access-token") String token);

    @DELETE("/order/{id}")
    Call<JsonObject> deleteOrder(@Header("x-access-token") String token, @Path("id") String orderId);
}

