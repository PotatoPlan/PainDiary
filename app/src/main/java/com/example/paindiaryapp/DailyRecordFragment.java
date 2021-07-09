package com.example.paindiaryapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paindiaryapp.databinding.FragmentDailyRecordBinding;
import com.example.paindiaryapp.entity.PainRecord;
import com.example.paindiaryapp.viewmodel.PainRecordViewModel;


import java.util.ArrayList;
import java.util.List;

public class DailyRecordFragment extends Fragment {
    private FragmentDailyRecordBinding binding;
    private PainRecordViewModel painRecordViewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Records> re;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDailyRecordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        // Populating the ViewModel
        painRecordViewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                // Initialise the ArrayList Records
                re = new ArrayList<Records>();
                for (PainRecord temp : painRecords) {
                    // Instantiate the Records Model
                    Records records = new Records(temp.dateOfEntry, temp.painLocation, temp.moodLevel, temp.stepsTaken, temp.painIntensity);
                    // Add each record to this Records ArrayList
                    re.add(records);

                    // Pass the data to the RecyclerViewAdapter (by initialising the constructor)
                    recyclerViewAdapter = new RecyclerViewAdapter(requireActivity(), re);
                    binding.recyclerView.setAdapter(recyclerViewAdapter);
                    layoutManager = new LinearLayoutManager(requireContext());
                    binding.recyclerView.setLayoutManager(layoutManager);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
