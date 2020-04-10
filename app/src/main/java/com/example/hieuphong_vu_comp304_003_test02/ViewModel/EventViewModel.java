package com.example.hieuphong_vu_comp304_003_test02.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Event;
import com.example.hieuphong_vu_comp304_003_test02.Repository.EventRepository;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private EventRepository eventRepository;
    private LiveData<List<Event>> allEvents;
    private LiveData<List<String>> allEventNames;
    private List<Event> eventsByName;

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository=new EventRepository(application);
        allEvents=eventRepository.getAllEvents();
        allEventNames=eventRepository.getAllEventNames();
    }

    public void insertEvent(Event event){
        eventRepository.insertEvent(event);
    }

    public LiveData<List<Event>> getAllEvents(){return allEvents;}

    public LiveData<List<String>> getAllEventNames(){return allEventNames;}

    public List<Event> getEventsByName(String event_name){return eventRepository.getEventsByName(event_name);}
}
