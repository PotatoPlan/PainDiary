package com.example.paindiaryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.paindiaryapp.databinding.ReportsPainLocationBinding;
import com.example.paindiaryapp.entity.PainRecord;
import com.example.paindiaryapp.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ReportsPainLocation extends Fragment {
    private ReportsPainLocationBinding binding;
    private PainRecordViewModel painRecordViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ReportsPainLocationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();



        // Populating the ViewModel
        painRecordViewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                // Initialise the Pie Chart Entries
                List<PieEntry> entries = new ArrayList<>();
                Map<String, Integer> painCount = new HashMap<>();
                for (PainRecord temp : painRecords) {
                    String painLocate = temp.painLocation;
                    // Counters will increase if there are more data entries
                    painCount.put(painLocate, painCount.getOrDefault(painLocate, 0) + 1);
                }
                
// --------------------For the Pie Chart, only non-zero values will be included in the chart--------------------

                for (String location : painCount.keySet()) {
                    int val = painCount.get(location)
                    if (val != 0) {
                        entries.add(new PieEntry(val, location));
                    }
                }

                PieDataSet set = new PieDataSet(entries, "");
                // Set the percentage value larger
                set.setValueTextSize(20f);
                // Use the color template to colour the chart
                set.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(set);

                // Add the description to the chart
                Description description = new Description();
                description.setText("Accumulated Pain Locations (%)");
                binding.painLocationPie.setDescription(description);

                binding.painLocationPie.setData(data);
                // Set the label size larger
                binding.painLocationPie.setEntryLabelTextSize(15f);
                // Use Percentage Values
                binding.painLocationPie.setUsePercentValues(true);

                binding.painLocationPie.invalidate();


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
