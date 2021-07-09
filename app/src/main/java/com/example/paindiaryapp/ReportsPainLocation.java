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
                // Set Counters for each pain location
                int numberOfBack = 0;
                int numberOfNeck = 0;
                int numberOfHead = 0;
                int numberOfKnees = 0;
                int numberOfHips = 0;
                int numberOfAbdomen = 0;
                int numberOfElbows = 0;
                int numberOfShoulders = 0;
                int numberOfShins = 0;
                int numberOfJaw = 0;
                int numberOfFacial = 0;
                for (PainRecord temp : painRecords) {
                    // Counters will increase if there are more data entries
                    switch (temp.painLocation) {
                        case "back":
                            numberOfBack++;
                            break;
                        case "neck":
                            numberOfNeck++;
                            break;
                        case "head":
                            numberOfHead++;
                            break;
                        case "knees":
                            numberOfKnees++;
                            break;
                        case "hips":
                            numberOfHips++;
                            break;
                        case "abdomen":
                            numberOfAbdomen++;
                            break;
                        case "elbows":
                            numberOfElbows++;
                            break;
                        case "shoulders":
                            numberOfShoulders++;
                            break;
                        case "shins":
                            numberOfShins++;
                            break;
                        case "jaw":
                            numberOfJaw++;
                            break;
                        default:
                            numberOfFacial++;
                            break;
                    }
                }

// --------------------For the Pie Chart, only non-zero values will be included in the chart--------------------
                if (numberOfBack > 0) {
                    entries.add(new PieEntry(numberOfBack, "Back"));
                }
                if (numberOfNeck > 0 ) {
                    entries.add(new PieEntry(numberOfNeck, "Neck"));
                }
                if (numberOfHead > 0 ) {
                    entries.add(new PieEntry(numberOfHead, "Head"));
                }
                if (numberOfKnees > 0 ) {
                    entries.add(new PieEntry(numberOfKnees, "Knees"));
                }
                if (numberOfHips > 0 ) {
                    entries.add(new PieEntry(numberOfHips, "Hips"));
                }
                if (numberOfAbdomen > 0 ) {
                    entries.add(new PieEntry(numberOfAbdomen, "Abdomen"));
                }
                if (numberOfElbows > 0 ) {
                    entries.add(new PieEntry(numberOfElbows, "Elbows"));
                }
                if (numberOfShoulders > 0 ) {
                    entries.add(new PieEntry(numberOfShoulders, "Shoulders"));
                }
                if (numberOfShins > 0 ) {
                    entries.add(new PieEntry(numberOfShins, "Shins"));
                }
                if (numberOfJaw > 0 ) {
                    entries.add(new PieEntry(numberOfJaw, "Jaw"));
                }
                if (numberOfFacial > 0 ) {
                    entries.add(new PieEntry(numberOfFacial, "Facial"));
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
