package com.example.hieuphong_vu_comp304_003_test02.Entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"userName"},unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String userName;
    private String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User prepopulateAdminUser(){
        User adminUser=new User();
        adminUser.setUserName("Admin");
        adminUser.setPassword("123456");
        return adminUser;
    }
}

