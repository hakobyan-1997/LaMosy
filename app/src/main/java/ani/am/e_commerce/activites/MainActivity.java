package ani.am.e_commerce.activites;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import ani.am.e_commerce.Global;
import ani.am.e_commerce.PrefConfig;
import ani.am.e_commerce.R;
import ani.am.e_commerce.api.ApiClient;
import ani.am.e_commerce.api.ApiInterface;
import ani.am.e_commerce.fragments.AllCategoriesFragment;
import ani.am.e_commerce.fragments.LoginFragment;
import ani.am.e_commerce.fragments.RegistrationFragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,LoginFragment.OnLoginFormActivityListener, RegistrationFragment.OnRegisterListener
{
    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.setLocaleLanguage(this,"hy");
        setContentView(R.layout.activity_main);
        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        this.configureDagger();
        Log.d("Tag","isLogin " + prefConfig.readLoginStatus());
       /* if(prefConfig.readLoginStatus()){
            startActivity(new Intent(this,BaseActivity.class));
            finish();
        }else{
            this.showFragment(savedInstanceState);
        }*/

        this.showFragment(savedInstanceState);
    }

    @Override
    protected void onResume() {
        getSupportActionBar().show();
        super.onResume();
    }

    @Override
    public void performRegister() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RegistrationFragment()).addToBackStack(null).commit();
    }

    @Override
    public void performLogin(String name, String token, String id) {
        prefConfig.writeName(name);
        prefConfig.writeToken(token,"token");
        prefConfig.writeToken(id,"id");
        startActivity(new Intent(this,BaseActivity.class));
        finish();
    }

    @Override
    public void registerPerformed(String name, String token,String id) {
        prefConfig.writeName(name);
        prefConfig.writeToken(token,"token");
        prefConfig.writeToken(id,"id");
        startActivity(new Intent(this,BaseActivity.class));
        finish();
    }


    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void configureDagger(){
        AndroidInjection.inject(this);
    }
    private void showFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {

            AllCategoriesFragment fragment = new AllCategoriesFragment();

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
