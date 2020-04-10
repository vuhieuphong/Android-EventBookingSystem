package com.example.hieuphong_vu_comp304_003_test02.Repository;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.hieuphong_vu_comp304_003_test02.Activity.AddUpdDelBookingActivity;
import com.example.hieuphong_vu_comp304_003_test02.AppDatabase;
import com.example.hieuphong_vu_comp304_003_test02.Dao.BookingDao;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Booking;

import java.util.List;

public class BookingRepository {
    private BookingDao bookingDao;
    private LiveData<List<Booking>> bookingsList;

    public BookingRepository(Context context){
        AppDatabase db=AppDatabase.getInstance(context);
        bookingDao=db.bookingDao();
        bookingsList=bookingDao.getAllBookings();
    }

    public LiveData<List<Booking>> getAllBookings(){return  bookingsList;}

    public void insertBooking(Booking booking){insertAsync(booking);}
    public void insertAsync(final Booking booking){
        new InsertAsyncTask(bookingDao).execute(booking);
    }

    private static class InsertAsyncTask extends AsyncTask<Booking, Void, Void> {
        private BookingDao mAsyncBookingDao;
        private boolean valid=true;

        InsertAsyncTask(BookingDao dao) {
            mAsyncBookingDao = dao;
        }

        @Override
        protected Void doInBackground(final Booking... bookings) {
            try{
                mAsyncBookingDao.insertBooking(bookings[0]);
            }
            catch (Exception e){
                valid=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            if(valid==false){
                Toast.makeText(AddUpdDelBookingActivity.bookingContext,"Constraint Not Met",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateBooking(Booking booking){updateAsync(booking);}
    public void updateAsync(final Booking booking){
        new UpdateAsyncTask(bookingDao).execute(booking);
    }

    private static class UpdateAsyncTask extends AsyncTask<Booking, Void, Void> {
        private BookingDao mAsyncBookingDao;
        private boolean valid=true;

        UpdateAsyncTask(BookingDao dao) {
            mAsyncBookingDao = dao;
        }

        @Override
        protected Void doInBackground(final Booking... bookings) {
            try{
                mAsyncBookingDao.updateBooking(bookings[0]);
            }
            catch (Exception e){
                valid=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            if(valid==false){
                Toast.makeText(AddUpdDelBookingActivity.bookingContext,"Constraint Not Met",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteBooking(Booking booking){deleteAsync(booking);}
    public void deleteAsync(final Booking booking){
        new DeleteAsyncTask(bookingDao).execute(booking);
    }

    private static class DeleteAsyncTask extends AsyncTask<Booking, Void, Void> {
        private BookingDao mAsyncBookingDao;
        private boolean valid=true;

        DeleteAsyncTask(BookingDao dao) {
            mAsyncBookingDao = dao;
        }

        @Override
        protected Void doInBackground(final Booking... bookings) {
            try{
                mAsyncBookingDao.deleteBooking(bookings[0]);
            }
            catch (Exception e){
                valid=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            if(valid==false){
                Toast.makeText(AddUpdDelBookingActivity.bookingContext,"Constraint Not Met",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<Booking> getBookingsByEventIdUserId(int event_id,int user_id){
        List<Booking> bookingsListByEventIdUserId=null;
        try {
            bookingsListByEventIdUserId=new GetBookingsByEventIdUserIdAsync(bookingDao,event_id,user_id).execute().get();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return bookingsListByEventIdUserId;
    }

    private static class GetBookingsByEventIdUserIdAsync extends AsyncTask<Void, Void, List<Booking>> {
        private BookingDao mAsyncBookingDao;
        private int eventId;
        private int userId;

        GetBookingsByEventIdUserIdAsync(BookingDao dao,int eventId,int userId) {
            mAsyncBookingDao = dao;
            this.eventId=eventId;
            this.userId=userId;
        }

        @Override
        protected List<Booking> doInBackground(final Void... voids) {
            return mAsyncBookingDao.getBookingsByEventIdUserId(eventId,userId);
        }
    }
}
