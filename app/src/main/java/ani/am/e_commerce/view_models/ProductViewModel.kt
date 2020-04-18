package ani.am.e_commerce.view_models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import ani.am.e_commerce.db.entity.Product
import ani.am.e_commerce.repositories.ProductRepository
import okhttp3.RequestBody
import javax.inject.Inject


class ProductViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    fun addProduct(categoryId: String, map: Map<String, RequestBody>) {
        productRepository.addProduct(categoryId, map)
    }

    fun deleteProduct(product: Product) {
        productRepository.deleteProduct(product)
    }

    fun updateProduct(categoryId: String, map: Map<String, RequestBody>) {
        productRepository.updateProduct(categoryId, map)
    }

    fun allProductsListByCategoryId(id:String) : LiveData<List<Product>>{
        return productRepository.getAllProductsByCategoryId(id)
    }
}
