package ani.am.e_commerce.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class Product {
    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("picture")
    private String picture;
    @SerializedName("price")
    private int price;
    @SerializedName("description")
    private String description;
    @SerializedName("stars")
    private int stars;
    @SerializedName("color")
    private String color;
    @SerializedName("size")
    private int size;
    @SerializedName("discount")
    private String discount;
    @SerializedName("gender")
    private String gender;
    @ForeignKey(entity = Category.class,parentColumns = "id", childColumns = "categoryId")
    @SerializedName("categoryId")
    private String categoryId;

    @TypeConverter
    public static Product getProduct() {
        return new Product();
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", stars=" + stars +
                ", color='" + color + '\'' +
                ", size=" + size +
                ", discount='" + discount + '\'' +
                ", gender='" + gender + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
