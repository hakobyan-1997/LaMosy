package ani.am.e_commerce.fragments;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ani.am.e_commerce.activites.MainActivity;
import ani.am.e_commerce.R;
import ani.am.e_commerce.db.entity.LoginResponse;
import ani.am.e_commerce.db.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {
    OnLoginFormActivityListener loginFormActivityListener;
    private EditText userName, userPassword;

    public  interface OnLoginFormActivityListener{
        public void performRegister();
        public void performLogin(String name, String token, String id);
    }

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
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
        switch (v.getId()){
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
        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }
    private void performLogin(){
        String username = userName.getText().toString();
        String userpassword = userPassword.getText().toString();
        User user = new User(username,userpassword);
        Call<LoginResponse> call = MainActivity.apiInterface.performUserLogin(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    String name = response.body().getUser().getName();
                    String token = response.body().getUser().getToken();
                    String id = response.body().getUser().getId();
                    loginFormActivityListener.performLogin(name,token,id);
                    MainActivity.prefConfig.writeLoginStatus(true);
                    userName.setText("");
                    userPassword.setText("");
                }else
                MainActivity.prefConfig.displayToast(getString(R.string.try_again));
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("Tag",t.getMessage());
                Toast.makeText(getContext(),getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)getActivity()).getSupportActionBar().show();
    }
}
