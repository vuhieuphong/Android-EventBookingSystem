package com.example.hieuphong_vu_comp304_003_test02.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Organizer;

import java.util.List;

@Dao
public interface OrganizerDao {
    @Insert
    void insertOrganizer(Organizer organizer);

    @Query("select * from organizer")
    LiveData<List<Organizer>> getAllOrganizers();
}
