package ani.am.e_commerce.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import ani.am.e_commerce.db.entity.Product;

public class ProductConverter {
    @TypeConverter
    public static List<Product> storedStringToProducts(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Product>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String myObjectsToStoredString(List<Product> products) {
        Gson gson = new Gson();
        return gson.toJson(products);
    }

    @TypeConverter
    public static Product storedStringToProduct(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new Product();
        }
        Type listType = new TypeToken<Product>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String myObjectToStoredString(Product product) {
        Gson gson = new Gson();
        return gson.toJson(product);
    }
}
