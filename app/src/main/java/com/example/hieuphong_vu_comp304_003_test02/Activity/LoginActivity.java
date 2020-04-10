package com.example.hieuphong_vu_comp304_003_test02.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.hieuphong_vu_comp304_003_test02.Entity.User;
import com.example.hieuphong_vu_comp304_003_test02.R;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    UserViewModel userViewModel;
    User user;
    EditText editTextLoginUserName;
    EditText editTextLoginPassword;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLoginUserName=(EditText)findViewById(R.id.editTextLoginUserName);
        editTextLoginPassword=(EditText)findViewById(R.id.editTextLoginPassword);

        userViewModel= ViewModelProviders.of(this).get(UserViewModel.class);
        user=new User();

        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        if(pref.contains("userId")&&pref.contains("username") && pref.contains("password")){
            Toast.makeText(getApplicationContext(),"Welcome "+pref.getString("username",null),Toast.LENGTH_SHORT)
                    .show();
            Intent mainActivity=new Intent(this,MainActivity.class);
            startActivity(mainActivity);
        }

        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String loginUserName=editTextLoginUserName.getText().toString();
                    String loginPass=editTextLoginPassword.getText().toString();
                    User userByUserNamePass=userViewModel.getUserByUserNamePass(loginUserName,loginPass);
                    if(userByUserNamePass!=null){
                        //saving into shared preference
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("userId",userByUserNamePass.getUserId());
                        editor.putString("username",loginUserName);
                        editor.putString("password",loginPass);
                        editor.commit();

                        Toast.makeText(getApplicationContext(),"Welcome "+pref.getString("username",null),Toast.LENGTH_SHORT)
                                .show();

                        Intent mainActivity=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(mainActivity);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button btnNewUser=(Button)findViewById(R.id.btnNewUser);
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });
    }
}
