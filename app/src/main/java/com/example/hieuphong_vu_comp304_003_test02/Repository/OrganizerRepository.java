package com.example.hieuphong_vu_comp304_003_test02.Repository;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.hieuphong_vu_comp304_003_test02.Activity.CreateOrganizerActivity;
import com.example.hieuphong_vu_comp304_003_test02.AppDatabase;
import com.example.hieuphong_vu_comp304_003_test02.Dao.OrganizerDao;
import com.example.hieuphong_vu_comp304_003_test02.Entity.Organizer;

import java.util.List;

public class OrganizerRepository {
    private OrganizerDao organizerDao;
    private LiveData<List<Organizer>> organizersList;

    public OrganizerRepository(Context context){
        AppDatabase db=AppDatabase.getInstance(context);
        organizerDao=db.organizerDao();
        organizersList=organizerDao.getAllOrganizers();
    }

    public LiveData<List<Organizer>> getAllOrganizers(){return  organizersList;}

    public void insertOrganizer(Organizer organizer){insertAsync(organizer);}

    public void insertAsync(final Organizer organizer){
        new InsertAsyncTask(organizerDao).execute(organizer);
    }

    private static class InsertAsyncTask extends AsyncTask<Organizer, Void, Void> {
        private OrganizerDao mAsyncOrganizerDao;
        private boolean valid=true;

        InsertAsyncTask(OrganizerDao dao) {
            mAsyncOrganizerDao = dao;
        }

        @Override
        protected Void doInBackground(final Organizer... organizers) {
            try{
                mAsyncOrganizerDao.insertOrganizer(organizers[0]);
            } catch (Exception e){
                valid=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            if(valid==false){
                Toast.makeText(CreateOrganizerActivity.createOrganizerActivityContext,"Constraint Not Met",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
