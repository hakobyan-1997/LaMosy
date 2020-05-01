package ani.am.e_commerce.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ani.am.e_commerce.db.entity.Product;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProductDao {

    @Insert(onConflict = REPLACE)
    void saveProductList(List<Product> productList);

    @Insert(onConflict = REPLACE)
    void insertProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT * FROM Product WHERE id = :productId")
    LiveData<Product> getProductById(String productId);

    @Query("SELECT * FROM Product")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM Product WHERE categoryId = :categoryId")
    LiveData<List<Product>> getAllProductsByCategoryId(String categoryId);
}

