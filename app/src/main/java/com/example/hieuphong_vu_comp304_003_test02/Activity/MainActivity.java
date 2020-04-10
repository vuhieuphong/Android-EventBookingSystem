package com.example.hieuphong_vu_comp304_003_test02.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hieuphong_vu_comp304_003_test02.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);

        final Button btnCreatorLink=(Button)findViewById(R.id.btnCreatorLink);
        btnCreatorLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.linkedin.com/in/hieu-phong-vu";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        final Button btnLogout=(Button) findViewById(R.id.buttonLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear shared preference
                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.commit();

                Intent loginActivity=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        final Button btnCreateEvent=(Button)findViewById(R.id.buttonCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(prf.getString("username",null).equals("Admin")){
                    Intent intent=new Intent(MainActivity.this,CreateEventActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Must be Admin to access!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button btnCreateOrganizer=(Button)findViewById(R.id.buttonCreateOrganizer);
        btnCreateOrganizer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(prf.getString("username",null).equals("Admin")){
                    Intent intent=new Intent(MainActivity.this,CreateOrganizerActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Must be Admin to access!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button btnAddUpdateDeleteBooking=(Button)findViewById(R.id.btnAddUpdateDeleteBooking);
        btnAddUpdateDeleteBooking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddUpdDelBookingActivity.class);
                startActivity(intent);
            }
        });

        final Button btnViewBookingDetails=(Button)findViewById(R.id.btnViewBookingDetails);
        btnViewBookingDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ViewBookingDetailsActivity.class);
                startActivity(intent);
            }
        });

        final Button btnConfirmBooking=(Button)findViewById(R.id.btnConfirmBooking);
        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ConfirmBookingActivity.class);
                startActivity(intent);
            }
        });
    }
}
