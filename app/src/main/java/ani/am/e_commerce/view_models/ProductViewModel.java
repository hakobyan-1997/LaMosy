package ani.am.e_commerce.view_models;

import android.arch.lifecycle.ViewModel;

import java.util.Map;

import javax.inject.Inject;

import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.repositories.ProductRepository;
import okhttp3.RequestBody;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;

    @Inject
    public ProductViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(String categoryId, Map<String, RequestBody> map) {
        productRepository.addProduct(categoryId, map);
    }

    public void deleteProduct(Product product) {
        productRepository.deleteProduct(product);
    }

    public void updateProduct(String categoryId, Map<String, RequestBody> map) {
        productRepository.updateProduct(categoryId, map);
    }
}
