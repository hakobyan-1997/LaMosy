package ani.am.e_commerce.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Order {
    @NonNull
    @PrimaryKey
    @Expose
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("productId")
    private Product product;
    @Expose
    @SerializedName("productQuantity")
    private int productQuantity;
    @Expose
    @SerializedName("deliveryDate")
    private String deliveryDate;
    @Expose
    @SerializedName("sellerId")
    private String sellerId;
    @Expose
    @SerializedName("buyerId")
    private String buyerId;
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

    private String id;

    public Order() {
    }

    public Order(String id, int productQuantity, String sellerId, String city, String country, String address, String region, String phone, String buyerName) {
        this.id = id;
        this.productQuantity = productQuantity;
        this.sellerId = sellerId;
        this.city = city;
        this.country = country;
        this.address = address;
        this.region = region;
        this.phone = phone;
        this.buyerName = buyerName;
    }

    @NonNull
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(@NonNull String orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", productId='" + id + '\'' +
                ", productQuantity=" + productQuantity +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", region='" + region + '\'' +
                ", phone='" + phone + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", totalPrice=" + totalPrice +
                ", product=" + product +
                '}';
    }

}
