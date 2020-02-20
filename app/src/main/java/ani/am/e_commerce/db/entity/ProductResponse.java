package ani.am.e_commerce.db.entity;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Product product;


    public ProductResponse(boolean success, String message, Product product ) {
        this.success = success;
        this.message = message;
        this.product = product;
    }
    public ProductResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Product getProduct() {
        return product;
    }
}
