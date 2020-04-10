package com.example.hieuphong_vu_comp304_003_test02.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Booking;
import com.example.hieuphong_vu_comp304_003_test02.R;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.BookingViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmBookingActivity extends AppCompatActivity {

    BookingViewModel bookingViewModel;
    TextView textViewMessage;
    EditText editTextTo;
    EditText editTextSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        editTextTo=(EditText)findViewById(R.id.editTextTo);
        editTextSubject=(EditText)findViewById(R.id.editTextSubject);

        textViewMessage=(TextView)findViewById(R.id.textViewMessage);
        textViewMessage.setMovementMethod(new ScrollingMovementMethod());

        bookingViewModel= ViewModelProviders.of(this).get(BookingViewModel.class);

        bookingViewModel.getAllBookings().observe(this, new Observer<List<Booking>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable List<Booking> bookings) {
                String output="";
                output=String.format("%-7s %-8s %-5s %-5s %-6s\n","Booking","Quantity","User","Event","Fee");

                SharedPreferences prf=getSharedPreferences("user_details",MODE_PRIVATE);
                int currentUser=prf.getInt("userId",0);
                List<Booking> currentUserBookings=bookings.stream().filter(x->x.getUserId()==currentUser).collect(Collectors.toList());
                ArrayList<Booking> currentUserBookingsList=new ArrayList<>(currentUserBookings);

                double totalFee=0;
                for(Booking booking:currentUserBookingsList){
                    output+=String.format("%-7s %-8s %-5s %-5s %-6s\n"
                            ,booking.getBookingId(),booking.getQuantity(),booking.getUserId(),booking.getEventId(),
                            "$"+booking.getQuantity()*bookingViewModel.getEventFeeFromBookingId(booking.getBookingId()));
                    totalFee+=booking.getQuantity()*bookingViewModel.getEventFeeFromBookingId(booking.getBookingId());
                }
                output+="\nTotal Fee: $"+totalFee;
                textViewMessage.setText(output);
            }
        });

        final Button btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if(editTextTo.getText().toString().equals("")){
                        throw new Exception("Please enter Email!");
                    }
                    Intent it = new Intent(Intent.ACTION_SEND);
                    it.putExtra(Intent.EXTRA_EMAIL, new String[]{editTextTo.getText().toString()});
                    it.putExtra(Intent.EXTRA_SUBJECT,editTextSubject.getText().toString());
                    it.putExtra(Intent.EXTRA_TEXT,textViewMessage.getText());
                    it.setType("message/rfc822");
                    startActivity(Intent.createChooser(it,"Choose Mail App"));
                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button btnReturn=(Button)findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(ConfirmBookingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
