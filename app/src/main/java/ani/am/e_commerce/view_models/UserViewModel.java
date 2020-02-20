package ani.am.e_commerce.view_models;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import ani.am.e_commerce.repositories.UserRepo;

public class UserViewModel extends ViewModel {
    UserRepo userRepo;

    @Inject
    public UserViewModel(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void initSession(){
        userRepo.initSession();
    }
}
