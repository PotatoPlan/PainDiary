package com.example.paindiaryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.paindiaryapp.databinding.ReportsStepsTakenBinding;
import com.example.paindiaryapp.entity.PainRecord;
import com.example.paindiaryapp.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ReportsStepsTaken extends Fragment {
    private ReportsStepsTakenBinding binding;
    private PainRecordViewModel painRecordViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ReportsStepsTakenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        // Initialise the Pie Chart Entries
        List<PieEntry> entries = new ArrayList<>();

        painRecordViewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                // Get necessary variables: goal steps, current steps and remaining steps through the loop
                int targetSteps = 0;
                int currentSteps = 0;
                int remainingSteps = 0;
                for (PainRecord temp : painRecords) {
                    targetSteps = Integer.parseInt(temp.goalSteps);
                    currentSteps = Integer.parseInt(temp.stepsTaken);
                    remainingSteps = targetSteps - currentSteps;
                }

                // Only draw the Pie Chart when there are remaining steps (as in, remaining steps > 0)
                if (remainingSteps > 0) {
                    entries.add(new PieEntry(currentSteps, "Today's Steps"));
                    entries.add(new PieEntry(remainingSteps, "Remaining Steps"));
                } else {
                    // Display the congratulations message when the user has no remaining steps
                    binding.congratsMessage.setVisibility(View.VISIBLE);
                    binding.stepsTakenPie.setVisibility(View.INVISIBLE);
                }

                PieDataSet set = new PieDataSet(entries, "");
                set.setValueTextSize(20f);
                set.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(set);

                // Add the description to the chart
                Description description = new Description();
                description.setText("Daily Steps Comparison");
                binding.stepsTakenPie.setDescription(description);

                binding.stepsTakenPie.setData(data);
                binding.stepsTakenPie.setEntryLabelTextSize(15f);
                binding.stepsTakenPie.invalidate();
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
