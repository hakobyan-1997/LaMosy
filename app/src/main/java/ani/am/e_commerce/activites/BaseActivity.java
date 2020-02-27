package ani.am.e_commerce.activites;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import ani.am.e_commerce.R;
import ani.am.e_commerce.fragments.AddCategoryFragment;
import ani.am.e_commerce.fragments.ProfileFragment;
import ani.am.e_commerce.fragments.SettingsFragment;
import ani.am.e_commerce.interfaces.UpdatePageInterfase;
import ani.am.e_commerce.view_models.UserViewModel;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static ani.am.e_commerce.activites.MainActivity.prefConfig;

public class BaseActivity extends AppCompatActivity implements UpdatePageInterfase, HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle action;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        this.configureDagger();
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        init();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.profile);
        }
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        action = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(action);
        action.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return action.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack(null).commit();
                break;
            case R.id.addCategory:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddCategoryFragment()).addToBackStack(null).commit();
                break;
            case R.id.logout:
                performedLogOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void performedLogOut() {
        String token = prefConfig.readToken("token");
        userViewModel.logout(this, token);
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void updatePage(Context context) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
    }
}
