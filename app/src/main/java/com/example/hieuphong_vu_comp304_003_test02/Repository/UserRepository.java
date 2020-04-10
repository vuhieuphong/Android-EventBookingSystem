package com.example.hieuphong_vu_comp304_003_test02.Repository;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.hieuphong_vu_comp304_003_test02.Activity.CreateUserActivity;
import com.example.hieuphong_vu_comp304_003_test02.AppDatabase;
import com.example.hieuphong_vu_comp304_003_test02.Dao.UserDao;
import com.example.hieuphong_vu_comp304_003_test02.Entity.User;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> usersList;

    public UserRepository(Context context){
        AppDatabase db=AppDatabase.getInstance(context);
        userDao=db.userDao();
        usersList=userDao.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers(){return  usersList;}

    public void insertUser(User user){insertAsync(user);}

    public void insertAsync(final User user){
        new InsertAsyncTask(userDao).execute(user);
    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao mAsyncUserDao;
        private boolean valid=true;

        InsertAsyncTask(UserDao dao) {
            mAsyncUserDao = dao;
        }

        @Override
        protected Void doInBackground(final User... users) {
            try{
                mAsyncUserDao.insertUser(users[0]);
            } catch (Exception e){
                valid=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            if(valid==false){
                Toast.makeText(CreateUserActivity.createUserActivityContext,"Constraint Not Met",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public User getUserByUserNamePass(String user_name, String pass){
        User userByUserNamePass=null;
        try {
            userByUserNamePass=new CheckCredentialsAsync(userDao,user_name,pass).execute().get();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return userByUserNamePass;
    }

    private static class CheckCredentialsAsync extends AsyncTask<Void, Void, User> {
        private UserDao mAsyncUserDao;
        private String loginUserName;
        private String loginPass;

        CheckCredentialsAsync(UserDao dao,String loginUserName,String loginPass) {
            mAsyncUserDao = dao;
            this.loginUserName=loginUserName;
            this.loginPass=loginPass;
        }

        @Override
        protected User doInBackground(final Void... voids) {
            return mAsyncUserDao.getUserByUserNamePass(loginUserName,loginPass);
        }
    }
}
