package ani.am.e_commerce.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserCategory {
    @SerializedName("userCategory")
    private List<Category> category;
    @SerializedName("_id")
    private String id;

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserCategory{" +
                "category=" + category +
                ", id='" + id + '\'' +
                '}';
    }
}
