package com.example.hieuphong_vu_comp304_003_test02.Repository;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.hieuphong_vu_comp304_003_test02.Activity.CreateEventActivity;
import com.example.hieuphong_vu_comp304_003_test02.Activity.CreateUserActivity;
import com.example.hieuphong_vu_comp304_003_test02.AppDatabase;
import com.example.hieuphong_vu_comp304_003_test02.Dao.EventDao;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Event;

import java.util.List;

public class EventRepository {
    private EventDao eventDao;
    private LiveData<List<Event>> eventsList;
    private LiveData<List<String>> eventNamesList;

    public EventRepository(Context context){
        AppDatabase db=AppDatabase.getInstance(context);
        eventDao=db.eventDao();
        eventsList=eventDao.getAllEvents();
        eventNamesList=eventDao.getEventNames();
    }

    public LiveData<List<Event>> getAllEvents(){return  eventsList;}

    public LiveData<List<String>> getAllEventNames(){return eventNamesList;}

    public void insertEvent(Event event){insertAsync(event);}

    public void insertAsync(final Event event){
        new InsertAsyncTask(eventDao).execute(event);
    }

    private static class InsertAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncEventDao;
        private boolean valid=true;

        InsertAsyncTask(EventDao dao) {
            mAsyncEventDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... events) {
            try{
                mAsyncEventDao.insertEvent(events[0]);
            } catch (Exception e){
                valid=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            if(valid==false){
                Toast.makeText(CreateEventActivity.createEventActivityContext,"Constraint Not Met", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<Event> getEventsByName(String event_name){
        List<Event> eventListByName=null;
        try {
            eventListByName=new GetEventsByNameAsync(eventDao,event_name).execute().get();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return eventListByName;
    }

    private static class GetEventsByNameAsync extends AsyncTask<Void, Void, List<Event>> {
        private EventDao mAsyncEventDao;
        private String eventName;

        GetEventsByNameAsync(EventDao dao,String eventName) {
            mAsyncEventDao = dao;
            this.eventName=eventName;
        }

        @Override
        protected List<Event> doInBackground(final Void... voids) {
            return mAsyncEventDao.getEventsByName(eventName);
        }
    }
}
