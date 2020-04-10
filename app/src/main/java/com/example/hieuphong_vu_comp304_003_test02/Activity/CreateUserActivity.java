package com.example.hieuphong_vu_comp304_003_test02.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hieuphong_vu_comp304_003_test02.Entity.User;
import com.example.hieuphong_vu_comp304_003_test02.R;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.UserViewModel;

import java.util.List;

public class CreateUserActivity extends AppCompatActivity {

    UserViewModel userViewModel;
    User user;
    EditText editTextUserName;
    EditText editTextPassword;

    public static Context createUserActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        editTextUserName=(EditText)findViewById(R.id.editTextUserName);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);

        createUserActivityContext=getApplicationContext();

        final TextView textViewDisplayUsers=(TextView)findViewById(R.id.textViewDisplayUsers);
        textViewDisplayUsers.setMovementMethod(new ScrollingMovementMethod());

        userViewModel= ViewModelProviders.of(this).get(UserViewModel.class);
        user=new User();

        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                String output="";
                output=String.format("%-4s %-10s %-10s\n","Uid","Username","Password");
                for(User user:users){
                    output+=String.format("%-4s %-10s %-10s\n"
                            ,user.getUserId(),user.getUserName(),user.getPassword());
                }
                textViewDisplayUsers.setText(output);
            }
        });

        final Button btnCreateUser=(Button) findViewById(R.id.buttonCreateUser);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(editTextUserName.getText().toString().matches("")){
                        throw new Exception("User Name cannot be empty");
                    }
                    if(editTextPassword.getText().toString().matches("")){
                        throw new Exception("Password cannot be empty");
                    }
                    user.setUserName(editTextUserName.getText().toString());
                    user.setPassword(editTextPassword.getText().toString());
                    userViewModel.insertUser(user);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button btnReturn=(Button) findViewById(R.id.buttonReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
