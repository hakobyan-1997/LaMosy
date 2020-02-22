package ani.am.e_commerce.repositories

import android.util.Log
import ani.am.e_commerce.activites.MainActivity
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

    fun addProduct(id: String, map: Map<String?, RequestBody?>?) {
        val token = MainActivity.prefConfig.readToken("token")
        Log.d("Tag", "add category $id  token $token")
        executor.execute {
            apiInterface.createProduct(id, token, map).enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    Log.d("Tag", response.toString())
                    if (response.body() != null) {
                        Log.d("Tag", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {}
            })
        }
    }

    private fun saveData(json: String) {
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

    fun deleteProduct(product: Product) {
        val token = MainActivity.prefConfig.readToken("token")
        executor.execute {
            apiInterface.deleteProduct(token, product.id).enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    Log.d("Tag", "Product deleted from network !" + response.message())
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {}
            })
            productDao.deleteProduct(product)
        }
    }
}