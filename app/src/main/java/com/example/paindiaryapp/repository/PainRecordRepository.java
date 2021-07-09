package com.example.paindiaryapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.paindiaryapp.dao.PainRecordDAO;
import com.example.paindiaryapp.database.PainRecordDatabase;
import com.example.paindiaryapp.entity.PainRecord;

import java.util.List;

public class PainRecordRepository {
    private PainRecordDAO painRecordDAO;
    private LiveData<List<PainRecord>> allPainRecords;

    public PainRecordRepository(Application application) {
        PainRecordDatabase db = PainRecordDatabase.getInstance(application);
        painRecordDAO = db.painRecordDAO();
        allPainRecords = painRecordDAO.getAll();
    }

    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }

    public void insert(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.insert(painRecord);
            }
        });
    }

    public void updatePainRecord(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.updatePainRecord(painRecord);
            }
        });
    }
}
