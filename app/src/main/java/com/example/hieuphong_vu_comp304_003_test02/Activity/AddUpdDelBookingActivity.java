package com.example.hieuphong_vu_comp304_003_test02.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Booking;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Event;
import com.example.hieuphong_vu_comp304_003_test02.ExpandableListAdapter;
import com.example.hieuphong_vu_comp304_003_test02.R;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.BookingViewModel;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.EventViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AddUpdDelBookingActivity extends AppCompatActivity {
    TextView textViewBookings;

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<Event>> listHash;

    EventViewModel eventViewModel;
    BookingViewModel bookingViewModel;
    public static Context bookingContext;
    public static Context activityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_delete_booking);
        bookingContext=getApplicationContext();
        activityContext=this;

        textViewBookings=(TextView)findViewById(R.id.textViewBookingsInfo);
        textViewBookings.setMovementMethod(new ScrollingMovementMethod());

        listView=(ExpandableListView)findViewById(R.id.listViewItems);

        eventViewModel= ViewModelProviders.of(this).get(EventViewModel.class);
        bookingViewModel= ViewModelProviders.of(this).get(BookingViewModel.class);

        listDataHeader=new ArrayList<>();
        listHash=new HashMap<>();

        eventViewModel.getAllEventNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> eventNames) {
                listDataHeader.addAll(eventNames);

                int i=0;
                for(String eventName:eventNames){
                    List<Event> eventsByName=eventViewModel.getEventsByName(eventName);
                    listHash.put(listDataHeader.get(i),eventsByName);
                    i++;
                }

                listAdapter=new ExpandableListAdapter(getApplicationContext(),listDataHeader,listHash);
                listView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }
        });

        bookingViewModel.getAllBookings().observe(this, new Observer<List<Booking>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable List<Booking> bookings) {
                String output="";
                output=String.format("%-4s %-9s %-4s %-4s\n","Bid","Quantity","Uid","Eid");

                SharedPreferences prf=getSharedPreferences("user_details",MODE_PRIVATE);
                int currentUser=prf.getInt("userId",0);
                List<Booking> currentUserBookings=bookings.stream().filter(x->x.getUserId()==currentUser).collect(Collectors.toList());
                ArrayList<Booking> currentUserBookingsList=new ArrayList<>(currentUserBookings);

                for(Booking booking:currentUserBookingsList){
                    output+=String.format("%-4s %-9s %-4s %-4s\n"
                            ,booking.getBookingId(),booking.getQuantity(),booking.getUserId(),booking.getEventId());
                }
                textViewBookings.setText(output);
            }
        });

        final Button btnReturn=(Button)findViewById(R.id.buttonReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(AddUpdDelBookingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
