package ani.am.e_commerce.view_models;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import javax.inject.Inject;

import ani.am.e_commerce.db.entity.User;
import ani.am.e_commerce.repositories.UserRepo;

public class UserViewModel extends ViewModel {
    UserRepo userRepo;

    @Inject
    public UserViewModel(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void initSession(Context context){
        userRepo.initSession(context);
    }

    public void login(Context context, User user){
        userRepo.login(context,user);
    }

    public void registration(Context context,User user){
        userRepo.registration(context,user);
    }

    public void logout(Context context, String token){
        userRepo.logout(context,token);
    }
}
