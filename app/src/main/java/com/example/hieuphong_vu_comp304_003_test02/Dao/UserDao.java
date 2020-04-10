package com.example.hieuphong_vu_comp304_003_test02.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hieuphong_vu_comp304_003_test02.Entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("select * from user")
    LiveData<List<User>> getAllUsers();

    @Query("select * from user where userName=:username and password=:pass")
    User getUserByUserNamePass(String username,String pass);
}
