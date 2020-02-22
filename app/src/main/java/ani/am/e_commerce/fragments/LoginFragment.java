package ani.am.e_commerce.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import ani.am.e_commerce.activites.MainActivity;
import ani.am.e_commerce.R;
import ani.am.e_commerce.db.entity.User;
import ani.am.e_commerce.view_models.UserViewModel;
import dagger.android.support.AndroidSupportInjection;

public class LoginFragment extends Fragment implements View.OnClickListener {
    OnLoginFormActivityListener loginFormActivityListener;
    private Context context;
    private EditText userName, userPassword;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;

    public interface OnLoginFormActivityListener {
        public void performRegister();
    }

    public LoginFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        TextView regText = view.findViewById(R.id.register_txt);
        regText.setOnClickListener(this);
        Button loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        userName = view.findViewById(R.id.user_name);
        userPassword = view.findViewById(R.id.user_pass);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_txt:
                loginFormActivityListener.performRegister();
                break;
            case R.id.login_btn:
                performLogin();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }

    private void performLogin() {
        String username = userName.getText().toString();
        String userpassword = userPassword.getText().toString();
        User user = new User(username, userpassword);
        userViewModel.login(context, user);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).getSupportActionBar().show();
    }
}
