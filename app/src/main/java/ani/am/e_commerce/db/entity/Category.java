package ani.am.e_commerce.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class Category implements Serializable {
    @NonNull
    @PrimaryKey
    @SerializedName("productCategoryId")
    @Expose
    private String id;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categoryImage")
    @Expose
    private String categoryPicture;
    @SerializedName("products")
    @Expose
    private List<Product> products;

    public Category(String categoryName, String categoryPicture) {
        this.categoryName = categoryName;
        this.categoryPicture = categoryPicture;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryPicture() {
        return categoryPicture;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryPicture(String categoryPicture) {
        this.categoryPicture = categoryPicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryPicture='" + categoryPicture + '\'' +
                ", id='" + id + '\'' +
               // ", products=" + products +
                '}';
    }
}
