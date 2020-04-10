package com.example.hieuphong_vu_comp304_003_test02.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Organizer;
import com.example.hieuphong_vu_comp304_003_test02.Repository.OrganizerRepository;

import java.util.List;

public class OrganizerViewModel extends AndroidViewModel {
    private OrganizerRepository organizerRepository;
    private LiveData<List<Organizer>> allOrganizers;

    public OrganizerViewModel(@NonNull Application application) {
        super(application);
        organizerRepository=new OrganizerRepository(application);
        allOrganizers=organizerRepository.getAllOrganizers();
    }

    public void insertOrganizer(Organizer organizer){
        organizerRepository.insertOrganizer(organizer);
    }

    public LiveData<List<Organizer>> getAllOrganizers(){return allOrganizers;}

}
