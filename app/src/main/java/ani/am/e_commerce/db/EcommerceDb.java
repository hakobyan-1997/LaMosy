package ani.am.e_commerce.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import ani.am.e_commerce.db.converter.ProductConverter;
import ani.am.e_commerce.db.dao.CategoryDao;
import ani.am.e_commerce.db.dao.ProductDao;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;

@Database(entities = {Category.class , Product.class}, version = 2, exportSchema = false)
@TypeConverters(ProductConverter.class)
public abstract class EcommerceDb extends RoomDatabase {
    private static volatile EcommerceDb INSTANCE;
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
}

