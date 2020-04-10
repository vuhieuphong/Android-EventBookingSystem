package com.example.hieuphong_vu_comp304_003_test02.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hieuphong_vu_comp304_003_test02.Entity.User;
import com.example.hieuphong_vu_comp304_003_test02.Repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;


    public UserViewModel(@NonNull Application application){
        super(application);
        userRepository=new UserRepository(application);
        allUsers=userRepository.getAllUsers();
    }

    public void insertUser(User user){
        userRepository.insertUser(user);
    }

    public LiveData<List<User>> getAllUsers(){return allUsers;}

    public User getUserByUserNamePass(String user_name, String pass){return userRepository.getUserByUserNamePass(user_name,pass);}
}
