package com.example.paindiaryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.paindiaryapp.database.PainRecordDatabase;
import com.example.paindiaryapp.databinding.FragmentReportsBinding;
import com.example.paindiaryapp.entity.PainRecord;
import com.example.paindiaryapp.viewmodel.PainRecordViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ReportsFragment extends Fragment {
    private FragmentReportsBinding binding;
    private FragmentTransaction transaction;
    private PainRecordViewModel painRecordViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inflate the Pain Location page once enter this fragment
        inflatePainLocation();

        // Bottom Navigation variables
        BottomNavigationView bottomNavigationView = binding.bottomNav;
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pain_location:
                    inflatePainLocation();
                    break;
                case R.id.steps_taken:
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.displaying_container, new ReportsStepsTaken()).commit();
                    break;
                case R.id.line_chart:
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.displaying_container, new ReportsLineChart()).commit();
            }
            return true;
        }
    };

    private void inflatePainLocation() {
        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.displaying_container, new ReportsPainLocation()).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
