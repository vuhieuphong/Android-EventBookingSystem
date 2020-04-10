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

import com.example.hieuphong_vu_comp304_003_test02.Entity.Event;
import com.example.hieuphong_vu_comp304_003_test02.R;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.EventViewModel;

import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    EventViewModel eventViewModel;
    Event event;
    EditText editTextEventName;
    EditText editTextFee;
    EditText editTextTime;
    EditText editTextLocation;
    EditText editTextOrgId;

    public static Context createEventActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        editTextEventName=(EditText)findViewById(R.id.editTextEventName);
        editTextFee=(EditText)findViewById(R.id.editTextFee);
        editTextTime=(EditText)findViewById(R.id.editTextTime);
        editTextLocation=(EditText)findViewById(R.id.editTextLocation);
        editTextOrgId=(EditText)findViewById(R.id.editTextOrgId);

        createEventActivityContext=getApplicationContext();

        final EditText textViewDisplayEvents=(EditText) findViewById(R.id.textViewDisplayEvents);
        textViewDisplayEvents.setHorizontallyScrolling(true);
        textViewDisplayEvents.setMovementMethod(new ScrollingMovementMethod());

        eventViewModel= ViewModelProviders.of(this).get(EventViewModel.class);
        event=new Event();

        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                String output="";
                output=String.format("%-4s %-15s %-7s %-8s %-15s %-4s\n","Eid","Event Name","Fee","Time","Location","Oid");
                for(Event event:events){
                    output+=String.format("%-4s %-15s %-7s %-8s %-15s %-4s\n"
                            ,event.getEventId(),event.getEventName(),"$"+event.getFee(),
                            event.getTime(),event.getLocation(),event.getOrgId());
                }
                textViewDisplayEvents.setText(output);
            }
        });

        final Button btnCreateEvent=(Button) findViewById(R.id.buttonCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(editTextEventName.getText().toString().matches("")){
                        throw new Exception("Event Name cannot be empty");
                    }
                    if(editTextOrgId.getText().toString().matches("")){
                        throw new Exception("Organizer Id cannot be empty");
                    }
                    event.setEventName(editTextEventName.getText().toString());
                    event.setFee(Double.parseDouble(editTextFee.getText().toString()));
                    event.setTime(editTextTime.getText().toString());
                    event.setLocation(editTextLocation.getText().toString());
                    event.setOrgId(Integer.parseInt(editTextOrgId.getText().toString()));
                    eventViewModel.insertEvent(event);
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
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
