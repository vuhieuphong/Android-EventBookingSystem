package com.example.hieuphong_vu_comp304_003_test02;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.hieuphong_vu_comp304_003_test02.Dao.BookingDao;
import com.example.hieuphong_vu_comp304_003_test02.Dao.EventDao;
import com.example.hieuphong_vu_comp304_003_test02.Dao.OrganizerDao;
import com.example.hieuphong_vu_comp304_003_test02.Dao.UserDao;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Booking;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Event;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Organizer;
import com.example.hieuphong_vu_comp304_003_test02.Entity.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Event.class, Booking.class, Organizer.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "EventBookingDB";
    public abstract UserDao userDao();
    public abstract EventDao eventDao();
    public abstract BookingDao bookingDao();
    public abstract OrganizerDao organizerDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            //Create database object
            INSTANCE = buildDatabase(context);
        }
        //to delete database:
        //context.deleteDatabase(DATABASE_NAME);
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).userDao().insertUser(User.prepopulateAdminUser());

                                for(Organizer org:Organizer.prepopulateOrganizers()){
                                    getInstance(context).organizerDao().insertOrganizer(org);
                                }

                                for(Event event:Event.prepopulateEvents()){
                                    getInstance(context).eventDao().insertEvent(event);
                                }
                            }
                        });
                    }
                })
                ////.fallbackToDestructiveMigration()
                .build();
    }
}
