package com.example.hieuphong_vu_comp304_003_test02.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Booking;
import com.example.hieuphong_vu_comp304_003_test02.Repository.BookingRepository;

import java.util.List;

public class BookingViewModel extends AndroidViewModel {
    private BookingRepository bookingRepository;
    private LiveData<List<Booking>> allBookings;

    public BookingViewModel(@NonNull Application application) {
        super(application);
        bookingRepository=new BookingRepository(application);
        allBookings=bookingRepository.getAllBookings();
    }

    public LiveData<List<Booking>> getAllBookings(){return allBookings;}

    public void insertBooking(Booking booking){
        bookingRepository.insertBooking(booking);
    }

    public void updateBooking(Booking booking){
        bookingRepository.updateBooking(booking);
    }

    public void deleteBooking(Booking booking){
        bookingRepository.deleteBooking(booking);
    }

    public List<Booking> getBookingsByEventIdUserId(int event_id,int user_id){return bookingRepository.getBookingsByEventIdUserId(event_id,user_id);}
}
