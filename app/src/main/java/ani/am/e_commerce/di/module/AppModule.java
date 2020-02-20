package ani.am.e_commerce.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import ani.am.e_commerce.api.ApiInterface;
import ani.am.e_commerce.db.EcommerceDb;
import ani.am.e_commerce.db.dao.CategoryDao;
import ani.am.e_commerce.db.dao.ProductDao;
import ani.am.e_commerce.repositories.CategoryRepository;
import ani.am.e_commerce.repositories.ProductRepository;
import ani.am.e_commerce.repositories.UserRepo;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ani.am.e_commerce.Constants.BASE_URL;

@Module(includes = ViewModelModule.class)
public class AppModule {

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    EcommerceDb provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                EcommerceDb.class, "Ecommerce.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    CategoryDao provideCategoryDao(EcommerceDb database) { return database.categoryDao(); }

    @Provides
    @Singleton
    ProductDao provdeProductDao(EcommerceDb database){
        return  database.productDao();
    }

    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    CategoryRepository provideCategoryRepository(ApiInterface apiInterface, CategoryDao categoryDao, Executor executor) {
        return new CategoryRepository(apiInterface, categoryDao, executor);
    }

    @Provides
    @Singleton
    ProductRepository provideProductRepository(ApiInterface apiInterface, ProductDao productDao, Executor executor) {
        return new ProductRepository(apiInterface, productDao, executor);
    }

    @Provides
    @Singleton
    UserRepo provideUserRepo(ApiInterface apiInterface, Executor executor){
        return new UserRepo(apiInterface,executor);
    }
    // --- NETWORK INJECTION ---


    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    ApiInterface provideApiWebservice(Retrofit restAdapter) {
        return restAdapter.create(ApiInterface.class);
    }
}