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
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

import ani.am.e_commerce.api.ApiInterface;
import ani.am.e_commerce.db.dao.CategoryDao;
import ani.am.e_commerce.db.dao.ProductDao;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ani.am.e_commerce.activites.MainActivity.prefConfig;

@Singleton
public class ProductRepository {
    private final ApiInterface apiInterface;
    private final ProductDao productDao;
    private final Executor executor;

    public ProductRepository(ApiInterface apiInterface, ProductDao productDao, Executor executor) {
        this.apiInterface = apiInterface;
        this.productDao = productDao;
        this.executor = executor;
    }

    public void addProduct(String id, Map<String, RequestBody> map){
        String token = prefConfig.readToken("token");
        Log.d("Tag","add category " + id + "  token " + token);
        executor.execute(()->
                apiInterface.createProduct(id,token,map).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", response.toString());
                        if(response.body() != null) {
                            Log.d("Tag", response.body().toString());
                           // saveData(response.body().toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                }));
    }
    private void saveData(String json) {
        Log.d("Tag", "data json " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            Gson gson = new Gson();
            Product product = gson.fromJson(data.toString(), Product.class);
            Log.d("Tag","updated Product " + product);
            productDao.insertProduct(product);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*public void updateProduct( Map<String, RequestBody> map){
        String token = prefConfig.readToken("token");
        String id = prefConfig.readToken("id");
        Log.d("Tag","update category " + id + "  token " + token);
        Log.d("Tag","update category " + map);
        executor.execute(()->
                apiInterface.updateCategory(id,token,map).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", response.toString());
                        if(response.body() != null)
                            Log.d("Tag", "updateCategory "+response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("Tag", "Fail updateCategory "+t.getMessage());
                    }
                }));
        //categoryDao.updateCategory();
    }*/

    public void deleteProduct(Product product) {
        String token = prefConfig.readToken("token");
        executor.execute(() -> {
                    apiInterface.deleteProduct(token, product.getId()).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d("Tag", "Product deleted from network !" + response.message());
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                   productDao.deleteProduct(product);
                }
        );
    }

    /*private void saveData(String json) {
        Log.d("Tag", "data json " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray data = jsonObject.getJSONArray("data");
            List<Category> categoryList = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                Category category = gson.fromJson(object.toString(), Category.class);
                categoryList.add(category);
            }
            Log.d("Tag", "categorylist size before save " + categoryList.size());
            categoryDao.saveList(categoryList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

}
