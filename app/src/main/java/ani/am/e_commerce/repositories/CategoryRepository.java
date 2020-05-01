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
import ani.am.e_commerce.db.entity.Category;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ani.am.e_commerce.activities.MainActivity.prefConfig;

@Singleton
public class CategoryRepository {

    private final ApiInterface apiInterface;
    private final CategoryDao categoryDao;
    private final Executor executor;

    public CategoryRepository(ApiInterface apiInterface, CategoryDao categoryDao, Executor executor) {
        this.apiInterface = apiInterface;
        this.categoryDao = categoryDao;
        this.executor = executor;
    }

    public LiveData<List<Category>> gatAllCategories() {
        refreshCategories();
        return categoryDao.getAllCategories();
    }

    public LiveData<List<Category>> getUserCategories() {
        refreshUserCategories();
        return categoryDao.getAllCategories();
    }

    public void addCategory(Map<String, RequestBody> map) {
        String token = prefConfig.readToken("token");
        Log.d("Tag", "add category token " + token);
        executor.execute(() ->
                apiInterface.createCategory(token, map).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", response.toString());
                        if (response.body() != null) {
                            insertData(response.body().toString());
                            Log.d("Tag", response.body().toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                }));
    }

    public void updateCategory(String id, Map<String, RequestBody> map) {
        String token = prefConfig.readToken("token");
        Log.d("Tag", "update category " + id + "  token " + token);
        Log.d("Tag", "update category " + map);
        executor.execute(() ->
                apiInterface.updateCategory(id, token, map).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", response.toString());
                        if (response.body() != null) {
                            Log.d("Tag", "updateCategory " + response.body().toString());
                            updateData(response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("Tag", "Fail updateCategory " + t.getMessage());
                    }
                }));
    }

    public void deleteCategory(Category category) {
        String token = prefConfig.readToken("token");
        executor.execute(() -> {
                    apiInterface.deleteCategory(token, category.getId()).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d("Tag", "Category deleted from network !" + response.message());
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                    categoryDao.deleteCategory(category);
                }
        );
    }

    private void searchCategory(String criteria) {
        executor.execute(() -> {
                    apiInterface.searchCategory(criteria).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d("Tag", "Category searched from network !" + response.message());
                            if(response.isSuccessful()){
                                saveData(response.body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                }
        );
    }

    public LiveData<List<Category>> searchableList(String criteria){
        searchCategory(criteria);
        return categoryDao.getAllCategories();
    }


    private void refreshCategories() {
        executor.execute(() ->
                apiInterface.getAllCategories().enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", "Category refreshed from network !");
                        executor.execute(() -> {
                            categoryDao.deleteCategoryList();
                            String json = response.body().toString();
                            saveData(json);
                        });
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                }));
    }

    private void refreshUserCategories() {
        String token = prefConfig.readToken("token");
        String id = prefConfig.readToken("id");
        Log.d("Tag", "token = " + token);
        Log.d("Tag", "id = " + id);
        executor.execute(() ->
                apiInterface.getCategories(token).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", "User category refreshed from network !" + response.toString());
                        executor.execute(() -> {
                            if (response.body() != null) {
                                categoryDao.deleteCategoryList();
                                String json = response.body().toString();
                                saveData(json);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                }));
    }

    private void saveData(String json) {
        executor.execute(() -> {
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
                categoryDao.saveList(categoryList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void insertData(String json) {
        executor.execute(() -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject data = jsonObject.getJSONObject("data");
                Gson gson = new Gson();
                Category category = gson.fromJson(data.toString(), Category.class);
                categoryDao.insertCategory(category);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateData(String json) {
        executor.execute(() -> {
            Log.d("Tag", "data json " + json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject data = jsonObject.getJSONObject("data");
                Gson gson = new Gson();
                Category category = gson.fromJson(data.toString(), Category.class);
                Log.d("Tag", "updated Category " + category);
                categoryDao.updateCategory(category);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
