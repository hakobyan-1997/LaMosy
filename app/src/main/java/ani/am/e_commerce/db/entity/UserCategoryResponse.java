package ani.am.e_commerce.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserCategoryResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<UserCategory> userCategory;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserCategory> getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(List<UserCategory> userCategory) {
        this.userCategory = userCategory;
    }

    @Override
    public String toString() {
        return "UserCategoryResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", userCategory=" + userCategory +
                '}';
    }
}
