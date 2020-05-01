package ani.am.e_commerce.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.support.annotation.NonNull
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
class Product : Serializable {
    @NonNull
    @PrimaryKey
    @SerializedName("productId")
    @Expose
    var id: String? = null
    @SerializedName("userId")
    @Expose
    var userId: String? = null
    @SerializedName("productName")
    @Expose
    var name: String? = null
    @SerializedName("productPicture")
    @Expose
    var picture: String? = null
    @SerializedName("productPrice")
    @Expose
    var price = 0
    @SerializedName("productDescription")
    @Expose
    var description: String? = null
    @SerializedName("productStars")
    @Expose
    var stars = 0
    @SerializedName("productItemsCount")
    @Expose
    var count = 0
    @SerializedName("productColor")
    @Expose
    var color: String? = null
    @SerializedName("productSize")
    @Expose
    var size = 0
    @SerializedName("productDiscount")
    @Expose
    var discount: String? = null
    @SerializedName("productModelGender")
    @Expose
    var gender: String? = null
    @ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["categoryId"])
    @SerializedName("productCategoryId")
    @Expose
    var categoryId: String? = null

    override fun toString(): String {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", stars=" + stars +
                ", count=" + count +
                ", color='" + color + '\'' +
                ", size=" + size +
                ", discount='" + discount + '\'' +
                ", gender='" + gender + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", userId='" + userId + '\'' +
                '}'
    }

    companion object {
        @get:TypeConverter
        val product: Product
            get() = Product()
    }
}