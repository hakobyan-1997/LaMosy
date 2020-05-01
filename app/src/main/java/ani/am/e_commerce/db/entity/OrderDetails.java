package ani.am.e_commerce.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetails {
    @SerializedName("productId")
    private String productId;
    @Expose
    @SerializedName("productQuantity")
    private int productQuantity;
    @Expose
    @SerializedName("sellerId")
    private String sellerId;
    @Expose
    @SerializedName("city")
    private String city;
    @Expose
    @SerializedName("country")
    private String country;
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("region")
    private String region;
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("buyerName")
    private String buyerName;
    @Expose
    @SerializedName("totalPrice")
    private int totalPrice;

    public OrderDetails(String productId, int productQuantity, String sellerId, String city, String country, String address, String region, String phone, String buyerName, int totalPrice) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.sellerId = sellerId;
        this.city = city;
        this.country = country;
        this.address = address;
        this.region = region;
        this.phone = phone;
        this.buyerName = buyerName;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                ", productId='" + productId + '\'' +
                ", productQuantity=" + productQuantity +
                ", sellerId='" + sellerId + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", region='" + region + '\'' +
                ", phone='" + phone + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
