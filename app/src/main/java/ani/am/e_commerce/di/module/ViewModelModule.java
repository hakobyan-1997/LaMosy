package ani.am.e_commerce.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ani.am.e_commerce.di.key.ViewModelKey;
import ani.am.e_commerce.view_models.CategoryViewModel;
import ani.am.e_commerce.view_models.FactoryViewModel;
import ani.am.e_commerce.view_models.ProductViewModel;
import ani.am.e_commerce.view_models.UserViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel.class)
    abstract ViewModel bindUserProfileViewModel(CategoryViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    abstract  ViewModel bindproductViewModel(ProductViewModel productViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}