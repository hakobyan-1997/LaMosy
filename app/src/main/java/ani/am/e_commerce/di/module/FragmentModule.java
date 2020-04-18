package ani.am.e_commerce.di.module;

import ani.am.e_commerce.fragments.AddCategoryFragment;
import ani.am.e_commerce.fragments.AddProductFragment;
import ani.am.e_commerce.fragments.AllCategoriesFragment;
import ani.am.e_commerce.fragments.LoginFragment;
import ani.am.e_commerce.fragments.OrdersFragment;
import ani.am.e_commerce.fragments.ProfileFragment;
import ani.am.e_commerce.fragments.RegistrationFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract AllCategoriesFragment contributeAllCategoriesFragment();
    @ContributesAndroidInjector
    abstract ProfileFragment contributeUserProfileFragment();
    @ContributesAndroidInjector
    abstract AddCategoryFragment contributeAddCategoryFragment();
    @ContributesAndroidInjector
    abstract AddProductFragment contributeAddProductFragment();
    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();
    @ContributesAndroidInjector
    abstract RegistrationFragment contributeRegistrationFragment();
    @ContributesAndroidInjector
    abstract OrdersFragment contributeOrderFragment();
}