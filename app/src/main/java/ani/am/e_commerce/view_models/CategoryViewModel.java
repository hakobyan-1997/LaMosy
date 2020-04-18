package ani.am.e_commerce.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.repositories.CategoryRepository;
import okhttp3.RequestBody;

public class CategoryViewModel extends ViewModel {
    private LiveData<List<Category>> categoryList;
    private CategoryRepository categoryListRepo;

    @Inject
    public CategoryViewModel(CategoryRepository categoryListRepo) {
        this.categoryListRepo = categoryListRepo;
    }

    public LiveData<List<Category>> getUserCategoriesList() {
        categoryList = categoryListRepo.getUserCategories();
        return this.categoryList;
    }

    public LiveData<List<Category>> getCategoriesList() {
        categoryList = categoryListRepo.gatAllCategories();
        return this.categoryList;
    }

    public void deleteCategory(Category category) {
        categoryListRepo.deleteCategory(category);
    }

    public void addCategory(Map<String, RequestBody> map) {
        categoryListRepo.addCategory(map);
    }

    public void updateCategory(String id, Map<String, RequestBody> map) {
        categoryListRepo.updateCategory(id, map);
    }
}
