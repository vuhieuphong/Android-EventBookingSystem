package com.example.hieuphong_vu_comp304_003_test02.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = Organizer.class,
        parentColumns = "orgId",
        childColumns = "orgId",onDelete = ForeignKey.CASCADE
),indices = {@Index(value = {"orgId"})})
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int eventId;
    private String eventName;
    private double fee;
    private String time;
    private String location;
    private int orgId;

    public Event(){}

    @Ignore
    public Event(String eventName,double fee,String time,String location,int orgId){
        this.eventName=eventName;
        this.fee=fee;
        this.time=time;
        this.location=location;
        this.orgId=orgId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public static List<Event> prepopulateEvents(){
        Event event1=new Event("BBQ Party",12.5,"Dec 4","Markham",1);
        Event event2=new Event("Job Fair",13,"Mar 12","Sheppard",2);
        Event event3=new Event("IT Fair",14.5,"Jan 7","King Street",4);
        Event event4=new Event("BBQ Party",11,"Dec 15","Queen Street",3);
        Event event5=new Event("Job Fair",10.5,"Mar 5","Scarborough",1);
        Event event6=new Event("IT Fair",20,"Apr 8","Bloor Street",4);
        Event event7=new Event("Fan Meeting",13,"Oct 23","Scarborough",2);
        Event event8=new Event("Pool Party",25.5,"Apr 1","Markham",3);
        Event event9=new Event("Fan Meeting",13,"Oct 23","Scarborough",2);
        Event event10=new Event("Pool Party",24.5,"Apr 11","Markham",3);
        Event event11=new Event("Fan Meeting",12,"Oct 20","Scarborough",1);
        Event event12=new Event("Pool Party",27.5,"Apr 13","King Street",4);

        return Arrays.asList(event1,event2,event3,event4,event5,event6,event7,event8,event9,event10,event11,event12);
    }
}


