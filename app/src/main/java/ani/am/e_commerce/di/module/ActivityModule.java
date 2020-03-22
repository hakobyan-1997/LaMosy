package ani.am.e_commerce.di.module;

import ani.am.e_commerce.activities.BaseActivity;
import ani.am.e_commerce.activities.MainActivity;
import ani.am.e_commerce.activities.UserProductsActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract BaseActivity contributeBaseActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract UserProductsActivity contributeUserProductsActivity();
}