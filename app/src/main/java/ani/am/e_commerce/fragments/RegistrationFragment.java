package ani.am.e_commerce.fragments;

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

public class RegistrationFragment extends Fragment {
    private EditText name,userName,userEmail, userPassword, userRetypePassword;
    private Button btnRegister;
    private Context context;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel = ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_registration, container, false);
        init(view);
        return  view;
    }

    private void init(View view) {
        name = view.findViewById(R.id.txt_name);
        userName = view.findViewById(R.id.txt_user_name);
        userPassword = view.findViewById(R.id.txt_user_pass);
        userRetypePassword = view.findViewById(R.id.txt_user_retype_pass);
        userEmail = view.findViewById(R.id.txt_user_email);
        btnRegister = view.findViewById(R.id.register_btn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistrtion();
            }
        });
        TextView loginTv = view.findViewById(R.id.login_txt);
        loginTv.setOnClickListener(v->  ((MainActivity)getActivity()).onBackPressed());
    }

    public void performRegistrtion(){
        String n = name.getText().toString();
        String username= userName.getText().toString();
        String password = userPassword.getText().toString();
        String retypepassword = userRetypePassword.getText().toString();
        String email = userEmail.getText().toString();
        if(checkInputData(n,username,email,password,retypepassword)) {
            User user = new User(n, username, email, password);
            userViewModel.registration(context,user);
        }
    }

    private boolean checkInputData(String name, String username, String email, String password, String retypepass){
        // եթե որևէ դաշտ լրացված չէ
        if(name.isEmpty()||username.isEmpty()||email.isEmpty()||password.isEmpty()||retypepass.isEmpty()){
            MainActivity.prefConfig.displayToast(getString(R.string.complete_all_fields));
            return false;
        }
        // եթե էլ-հասցեն չի պարունակում @ կամ ․
        if(!email.contains("@") || !email.contains(".")){
            MainActivity.prefConfig.displayToast(getString(R.string.invalid_email_address));
            userEmail.setText("");
            return false;
        }
        // եթե գաղտնաբառը սխալ է կրկնված
        if(!password.equals(retypepass)) {
            userPassword.setText("");
            userRetypePassword.setText("");
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
