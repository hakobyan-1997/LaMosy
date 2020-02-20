package ani.am.e_commerce.di.module;

import ani.am.e_commerce.fragments.AddCategoryFragment;
import ani.am.e_commerce.fragments.AddProductFragment;
import ani.am.e_commerce.fragments.AllCategoriesFragment;
import ani.am.e_commerce.fragments.ProfileFragment;
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
}