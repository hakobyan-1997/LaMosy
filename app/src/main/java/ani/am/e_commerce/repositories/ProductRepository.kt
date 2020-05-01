package ani.am.e_commerce.repositories

import android.arch.lifecycle.LiveData
import android.location.Criteria
import android.util.Log
import ani.am.e_commerce.activities.MainActivity
import ani.am.e_commerce.api.ApiInterface
import ani.am.e_commerce.db.dao.ProductDao
import ani.am.e_commerce.db.entity.Product
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import javax.inject.Singleton

@Singleton
class ProductRepository(
        private val apiInterface: ApiInterface,
        private val productDao: ProductDao,
        private val executor: Executor) {

    fun addProduct(id: String, map: Map<String, RequestBody>) {
        val token = MainActivity.prefConfig.readToken("token")
        Log.d("Tag", "add product $id  token $token")
        executor.execute {
            apiInterface.createProduct(id, token, map).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    executor.execute {
                        Log.d("Tag", response.toString())
                        if (response.body() != null) {
                            Log.d("Tag", response.body().toString())
                            saveData(response.body().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {}
            })
        }
    }

    fun updateProduct(id: String, map: Map<String, RequestBody>) {
        val token = MainActivity.prefConfig.readToken("token")
        Log.d("Tag", "update product $id  token $token")
        Log.d("Tag", "update product $map")
        executor.execute {
            apiInterface.updateProduct( id, token, map).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("Tag", response.toString())
                    if (response.body() != null) {
                        Log.d("Tag", "updateProduct " + response.body().toString())
                        updateData(response.body().toString());
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("Tag", "Fail updateProduct " + t.message)
                }
            })
        }
    }

    fun saveData(json: String) {
        executor.execute {
            Log.d("Tag", "data json $json")
            try {
                val jsonObject = JSONObject(json)
                val data = jsonObject.getJSONObject("data")
                val gson = Gson()
                val product = gson.fromJson(data.toString(), Product::class.java)
                Log.d("Tag", "updated Product $product")
                productDao.insertProduct(product)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    fun updateData(json: String) {
        executor.execute {
            Log.d("Tag", "data json $json")
            try {
                val jsonObject = JSONObject(json)
                val data = jsonObject.getJSONObject("data")
                val gson = Gson()
                val product = gson.fromJson(data.toString(), Product::class.java)
                Log.d("Tag", "updated Product $product")
                productDao.updateProduct(product)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    fun deleteProduct(product: Product) {
        val token = MainActivity.prefConfig.readToken("token")
        executor.execute {
            apiInterface.deleteProduct(token, product.id).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("Tag", "Product deleted from network !" + response.message())
                    if (response.isSuccessful)
                        executor.execute {
                            productDao.deleteProduct(product)
                        }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {}
            })
            productDao.deleteProduct(product)
        }
    }

    fun searchProduct(criteria: String){
        executor.execute(){
            apiInterface.searchProduct(criteria).enqueue(object :Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("Tag", "Product searched!" + response.message())
                    if (response.isSuccessful)
                        executor.execute {
                            saveProductsList(response.body().toString())
                        }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {}

            })
        }
    }

    fun getSearchableList (criteria: String):LiveData<List<Product>>{
        searchProduct(criteria)
        return productDao.allProducts
    }

    fun getAllProductsByCategoryId(id: String): LiveData<List<Product>> {
        refreshProductsListByCategoryId(id)
        return productDao.getAllProductsByCategoryId(id)
    }

    fun refreshProductsListByCategoryId(categoryId: String) {
        //val token = MainActivity.prefConfig.readToken("token")
        executor.execute {
            apiInterface.getAllProductByCategoryId(categoryId).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("Tag", "refreshProductsListByCategoryId $response.toString()")
                    if (response.body() != null) {
                        Log.d("Tag", response.body().toString())
                        saveProductsList(response.body().toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {}
            })
        }
    }

    fun saveProductsList(json: String) {
        val listProduct = arrayListOf<Product>()
        executor.execute {
            Log.d("Tag", "data json $json")
            val jsonObject = JSONObject(json)
            val data = jsonObject.getJSONArray("data")
            try {
                for (i in 0..data.length() - 1) {
                    val productObject = data.getJSONObject(i)
                    val gson = Gson()
                    val product = gson.fromJson(productObject.toString(), Product::class.java)
                    listProduct.add(product)
                }

                Log.d("Tag", "updated Product $listProduct")
                productDao.saveProductList(listProduct)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

}