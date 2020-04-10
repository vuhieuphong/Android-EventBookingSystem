package com.example.hieuphong_vu_comp304_003_test02.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Booking;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    void insertBooking(Booking booking);

    @Query("select * from booking")
    LiveData<List<Booking>> getAllBookings();

    @Update
    void updateBooking(Booking booking);

    @Delete
    void deleteBooking(Booking booking);

    @Query("select * from booking where eventId=:event_id and userId=:user_id")
    List<Booking> getBookingsByEventIdUserId(int event_id,int user_id);
}
