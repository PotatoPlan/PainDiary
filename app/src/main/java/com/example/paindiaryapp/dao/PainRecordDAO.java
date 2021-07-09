package com.example.paindiaryapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.paindiaryapp.entity.PainRecord;

import java.util.List;

@Dao
public interface PainRecordDAO {
    @Query("SELECT * FROM painrecord ORDER BY Date_of_Entry ASC")
    LiveData<List<PainRecord>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PainRecord painRecord);

    @Update
    void updatePainRecord(PainRecord painRecord);
}
