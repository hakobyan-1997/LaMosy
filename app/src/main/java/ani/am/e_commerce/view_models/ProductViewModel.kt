package ani.am.e_commerce.view_models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import ani.am.e_commerce.db.entity.Product
import ani.am.e_commerce.repositories.ProductRepository
import okhttp3.RequestBody
import javax.inject.Inject


class ProductViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    fun addProduct(id: String, map: Map<String, RequestBody>) {
        productRepository.addProduct(id, map)
    }

    fun deleteProduct(product: Product) {
        productRepository.deleteProduct(product)
    }

    fun updateProduct(id: String, map: Map<String, RequestBody>) {
        productRepository.updateProduct(id, map)
    }

    fun allProductsListByCategoryId(id:String) : LiveData<List<Product>>{
        return productRepository.getAllProductsByCategoryId(id)
    }

    fun searchProduct(criteria :String): LiveData<List<Product>>{
        return productRepository.getSearchableList(criteria)
    }
}
