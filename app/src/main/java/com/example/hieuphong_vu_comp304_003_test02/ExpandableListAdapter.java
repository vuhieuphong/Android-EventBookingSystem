package com.example.hieuphong_vu_comp304_003_test02;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.hieuphong_vu_comp304_003_test02.Activity.AddUpdDelBookingActivity;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Booking;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Event;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.BookingViewModel;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.EventViewModel;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,List<Event>> listHashMap;
    BookingViewModel bookingViewModel;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<Event>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        bookingViewModel=ViewModelProviders.of((FragmentActivity) AddUpdDelBookingActivity.activityContext)
                .get(BookingViewModel.class);
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle=(String)getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_header,null);
        }
        TextView textViewName=(TextView)convertView.findViewById(R.id.textViewName);
        textViewName.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Event childEvent=(Event)getChild(groupPosition,childPosition);
        String childText="Event"+childEvent.getEventId()+"-$"+childEvent.getFee()+"-"+childEvent.getTime()+"-"+childEvent.getLocation()+"-Org"+childEvent.getOrgId();
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_item,null);
        }
        TextView textViewDetails=(TextView)convertView.findViewById(R.id.textViewDetails);
        textViewDetails.setText(childText);

        EditText editTextQuantity=(EditText)convertView.findViewById(R.id.editTextQuantity);

        Button btnAdd=(Button)convertView.findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prf = context.getSharedPreferences("user_details",MODE_PRIVATE);
                int currentUserId=prf.getInt("userId",0);
                List<Booking> bookingInDatabase=bookingViewModel.getBookingsByEventIdUserId(childEvent.getEventId(),currentUserId);
                try{
                    if(editTextQuantity.getText().equals("")){
                        throw new Exception("Please enter quantity to add/subtract!");
                    }

                    int quantity=Integer.parseInt(editTextQuantity.getText().toString());
                    if(bookingInDatabase.size()==0){
                        Booking newBooking=new Booking();

                        newBooking.setUserId(currentUserId);
                        newBooking.setQuantity(quantity);
                        newBooking.setEventId(childEvent.getEventId());
                        bookingViewModel.insertBooking(newBooking);
                    }
                    else{
                        int prevQuantity=bookingInDatabase.get(0).getQuantity();
                        int newQuantity=prevQuantity+quantity;

                        Booking bookingUpdated=bookingInDatabase.get(0);
                        bookingUpdated.setQuantity(newQuantity);
                        bookingViewModel.updateBooking(bookingUpdated);
                    }
                }
                catch (Exception ex){
                    Toast.makeText(AddUpdDelBookingActivity.bookingContext,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnSub=(Button)convertView.findViewById(R.id.buttonSub);
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prf = context.getSharedPreferences("user_details",MODE_PRIVATE);
                int currentUserId=prf.getInt("userId",0);
                List<Booking> bookingInDatabase=bookingViewModel.getBookingsByEventIdUserId(childEvent.getEventId(),currentUserId);
                try{
                    if(editTextQuantity.getText().equals("")){
                        throw new Exception("Please enter quantity to add/subtract!");
                    }

                    int quantity=Integer.parseInt(editTextQuantity.getText().toString());
                    if(bookingInDatabase.size()==0){
                        throw new Exception("You do not have bookings of this event!");
                    }
                    else{
                        int prevQuantity=bookingInDatabase.get(0).getQuantity();
                        int newQuantity=prevQuantity-quantity;

                        if(newQuantity>0){
                            Booking bookingUpdated=bookingInDatabase.get(0);
                            bookingUpdated.setQuantity(newQuantity);
                            bookingViewModel.updateBooking(bookingUpdated);
                        }
                        else {
                            bookingViewModel.deleteBooking(bookingInDatabase.get(0));
                        }
                    }
                }
                catch (Exception ex){
                    Toast.makeText(AddUpdDelBookingActivity.bookingContext,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
