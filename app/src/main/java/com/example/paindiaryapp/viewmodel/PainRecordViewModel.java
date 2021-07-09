package com.example.paindiaryapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.paindiaryapp.entity.PainRecord;
import com.example.paindiaryapp.repository.PainRecordRepository;

import java.util.List;

public class PainRecordViewModel extends AndroidViewModel {
    private PainRecordRepository painRecordRepository;
    private LiveData<List<PainRecord>> allPainRecords;

    public PainRecordViewModel(Application application) {
        super(application);
        painRecordRepository = new PainRecordRepository(application);
        allPainRecords = painRecordRepository.getAllPainRecords();
    }

    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }

    public void insert(PainRecord painRecord) {
        painRecordRepository.insert(painRecord);
    }

    public void updatePainRecord(PainRecord painRecord) {
        painRecordRepository.updatePainRecord(painRecord);
    }
}
