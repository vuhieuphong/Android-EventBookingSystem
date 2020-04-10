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

import com.example.hieuphong_vu_comp304_003_test02.Entity.Organizer;
import com.example.hieuphong_vu_comp304_003_test02.R;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.OrganizerViewModel;

import java.util.List;

public class CreateOrganizerActivity extends AppCompatActivity {

    OrganizerViewModel organizerViewModel;
    Organizer organizer;
    EditText editTextOrgName;
    EditText editTextOrgLocation;

    public static Context createOrganizerActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organizer);

        editTextOrgName=(EditText)findViewById(R.id.editTextOrgName);
        editTextOrgLocation=(EditText)findViewById(R.id.editTextOrgLocation);

        final TextView textViewDisplayOrgs=(TextView)findViewById(R.id.textViewDisplayOrgs);
        textViewDisplayOrgs.setHorizontallyScrolling(true);
        textViewDisplayOrgs.setMovementMethod(new ScrollingMovementMethod());

        createOrganizerActivityContext=getApplicationContext();

        organizerViewModel= ViewModelProviders.of(this).get(OrganizerViewModel.class);
        organizer=new Organizer();

        organizerViewModel.getAllOrganizers().observe(this, new Observer<List<Organizer>>() {
            @Override
            public void onChanged(@Nullable List<Organizer> organizers) {
                String output="";
                output=String.format("%-4s %-20s %-20s\n","Oid","Org Name","Org Location");
                for(Organizer organizer:organizers){
                    output+=String.format("%-4s %-20s %-20s\n"
                            ,organizer.getOrgId(),organizer.getOrgName(),organizer.getOrgLocation());
                }
                textViewDisplayOrgs.setText(output);
            }
        });

        final Button btnCreateOrg=(Button) findViewById(R.id.buttonCreateOrganizer);
        btnCreateOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(editTextOrgName.getText().toString().matches("")){
                        throw new Exception("Organizer Name cannot be empty");
                    }
                    if(editTextOrgLocation.getText().toString().matches("")){
                        throw new Exception("Organizer Location cannot be empty");
                    }
                    organizer.setOrgName(editTextOrgName.getText().toString());
                    organizer.setOrgLocation(editTextOrgLocation.getText().toString());
                    organizerViewModel.insertOrganizer(organizer);
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
