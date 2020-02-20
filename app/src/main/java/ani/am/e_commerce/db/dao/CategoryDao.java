package ani.am.e_commerce.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CategoryDao {

    @Insert(onConflict = REPLACE)
    void insertCategory(Category category);

    @Insert(onConflict = REPLACE)
    void saveList(List<Category> categoryList);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("DELETE FROM Category")
    void deleteCategoryList();

    @Query("SELECT * FROM Category WHERE id = :categoryId")
    LiveData<Category> getCategoryById(String categoryId);

    @Query("SELECT * FROM Product WHERE categoryId = :categoryId")
    LiveData<List<Product>> getProductsByCategoryId(String categoryId);

    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getAllCategories();
}

