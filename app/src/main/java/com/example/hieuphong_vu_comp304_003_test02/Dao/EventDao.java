package com.example.hieuphong_vu_comp304_003_test02.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insertEvent(Event event);

    @Query("select * from event")
    LiveData<List<Event>> getAllEvents();

    @Query("select distinct eventName from event")
    LiveData<List<String>> getEventNames();

    @Query("select * from event where eventName=:event_name")
    List<Event> getEventsByName(String event_name);
}
