package ani.am.e_commerce.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import ani.am.e_commerce.Global;
import ani.am.e_commerce.PrefConfig;
import ani.am.e_commerce.R;
import ani.am.e_commerce.fragments.AllCategoriesFragment;
import ani.am.e_commerce.fragments.LoginFragment;
import ani.am.e_commerce.fragments.RegistrationFragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, LoginFragment.OnLoginFormActivityListener {
    public static PrefConfig prefConfig;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefConfig = new PrefConfig(this);
        Global.setLocaleLanguage(this, prefConfig.getLang());
        setContentView(R.layout.activity_main);
        this.configureDagger();
        Log.d("Tag", "isLogin " + prefConfig.readLoginStatus());
        if (prefConfig.readLoginStatus()) {
            startActivity(new Intent(this, BaseActivity.class));
            finish();
        } else {
            this.showFragment(savedInstanceState);
        }
    }

    @Override
    public void performRegister() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RegistrationFragment()).addToBackStack(null).commit();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void showFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            String href = getIntent().getStringExtra("href");
            Log.d("Tag", "href 1= " + href);
            AllCategoriesFragment fragment = AllCategoriesFragment.newInstance(href);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
