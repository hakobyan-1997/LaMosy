package ani.am.e_commerce.db.entity;

import com.google.gson.annotations.SerializedName;

public class CategoryResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Category category;


    public CategoryResponse(boolean success, String message,Category category ) {
        this.success = success;
        this.message = message;
        this.category = category;
    }
    public CategoryResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Category getCategory() {
        return category;
    }
}
