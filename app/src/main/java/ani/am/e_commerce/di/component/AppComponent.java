package ani.am.e_commerce.di.component;

import android.app.Application;

import javax.inject.Singleton;

import ani.am.e_commerce.App;
import ani.am.e_commerce.di.module.ActivityModule;
import ani.am.e_commerce.di.module.AppModule;
import ani.am.e_commerce.di.module.FragmentModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);
}