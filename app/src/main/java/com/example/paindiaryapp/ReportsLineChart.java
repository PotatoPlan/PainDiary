package com.example.paindiaryapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Entity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.paindiaryapp.databinding.ReportsLineChartBinding;
import com.example.paindiaryapp.entity.PainRecord;
import com.example.paindiaryapp.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportsLineChart extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ReportsLineChartBinding binding;
    private boolean startDate;
    private PainRecordViewModel painRecordViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ReportsLineChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();




        // For Spinner
        List<String> items = new ArrayList<>();
        items.add("Temperature");
        items.add("Humidity");
        items.add("Pressure");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        binding.spinner.setAdapter(spinnerAdapter);
        // For Spinner



        // Two buttons to open the date picker and set start and end dates
        binding.startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment startDatePicker = new DatePickerFragment();
                startDatePicker.show(getChildFragmentManager(), "start date");
                startDate = true;
            }
        });

        binding.endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endDatePicker = new DatePickerFragment();
                endDatePicker.show(getChildFragmentManager(), "end date");
                startDate = false;
            }
        });




// --------------------Get the selected start and end dates by using the SharedPreference--------------------
        SharedPreferences sharedPre = requireActivity().getSharedPreferences("Date", Context.MODE_PRIVATE);
        String sDate = sharedPre.getString("Start Date", "2021-01-01");
        String eDate = sharedPre.getString("End Date", "2021-01-01");
        // If the data is not empty in the SharedPreference, set them on the UI
        if (!sDate.isEmpty() && !eDate.isEmpty()) {
            binding.startDate.setText(sDate);
            binding.endDate.setText(eDate);
        }

        // Use them to set the X axis of the graph, so convert them into Integers
        int intStartDate = 0;
        int intEndDate = 0;

        // If they exist, them parse their format from yyyy-mm-dd to yyyymmdd (using the regex)
        if (!sDate.isEmpty() && !eDate.isEmpty()) {
            intStartDate = Integer.parseInt(sDate.replaceAll("\\D+",""));
            intEndDate = Integer.parseInt(eDate.replaceAll("\\D+",""));
        }

        // set the maximum and minimum value of the X axis
        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setAxisMinimum(intStartDate);
        xAxis.setAxisMaximum(intEndDate);




        // Populating the ViewModel
        painRecordViewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                // --------------------Initialise all ArrayLists--------------------
                ArrayList<Float> floatPains = new ArrayList<>();
                ArrayList<Float> floatTemperature = new ArrayList<>();
                ArrayList<Float> floatHumidity = new ArrayList<>();
                ArrayList<Float> floatPressure = new ArrayList<>();
                // For X axis data
                ArrayList<Integer> intDate = new ArrayList<>();
                // For x axis label
                ArrayList<String> xLabel = new ArrayList<>();

                // --------------------VARIABLES FOR CORRELATION--------------------
                ArrayList<Double> corPain = new ArrayList<>();
                ArrayList<Double> corTemperature = new ArrayList<>();
                ArrayList<Double> corHumidity = new ArrayList<>();
                ArrayList<Double> corPressure = new ArrayList<>();
                // --------------------VARIABLES FOR CORRELATION--------------------

                // Initialise all entries (weather data will change according to user's selection
                ArrayList<Entry> pains = new ArrayList<>();
                ArrayList<Entry> temperatures = new ArrayList<>();
                ArrayList<Entry> humidity = new ArrayList<>();
                ArrayList<Entry> pressures = new ArrayList<>();

                for (PainRecord temp : painRecords) {
                    // Store these float data into the entries
                    floatPains.add(Float.parseFloat(temp.painIntensity));
                    floatTemperature.add(Float.parseFloat(temp.temperature));
                    floatHumidity.add(Float.parseFloat(temp.humidity));
                    floatPressure.add(Float.parseFloat(temp.pressure));
                    // Using regex to extract only digits from the date (https://stackoverflow.com/questions/4030928/extract-digits-from-a-string-in-java/4030936)
                    intDate.add(Integer.parseInt(temp.dateOfEntry.replaceAll("\\D+","")));
                    // Store the date string as X axis labels
                    xLabel.add(temp.dateOfEntry);

                    // --------------------DATA FOR CORRELATION--------------------
                    corPain.add(Double.parseDouble(temp.painIntensity));
                    corTemperature.add(Double.parseDouble(temp.temperature));
                    corHumidity.add(Double.parseDouble(temp.humidity));
                    corPressure.add((Double.parseDouble(temp.pressure)));
                }

                // Store entries arraylist: integer date as X, float data as Y
                for (int i = 0; i < floatPains.size(); i++) {
                    pains.add(new Entry(intDate.get(i), floatPains.get(i)));
                }

                for (int i = 0; i < floatTemperature.size(); i++) {
                    temperatures.add(new Entry(intDate.get(i), floatTemperature.get(i)));
                }

                for (int i = 0; i < floatHumidity.size(); i++) {
                    humidity.add(new Entry(intDate.get(i), floatHumidity.get(i)));
                }

                for (int i = 0; i < floatPressure.size(); i++) {
                    pressures.add(new Entry(intDate.get(i), floatPressure.get(i)));
                }




                // --------------------For X axis labels--------------------
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabel));
                xAxis.setTextSize(5f);
                xAxis.setTextColor(Color.BLACK);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);




                // --------------------Display DIFFERENT graphs according to user's selection of weather variables (by using spinner)--------------------
                binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String userSelected = parent.getItemAtPosition(position).toString();
                        switch (userSelected) {
                            // When user selects Temperature
                            case "Temperature": {
                                YAxis leftAxis = binding.lineChart.getAxisLeft();
                                YAxis rightAxis = binding.lineChart.getAxisRight();

                                // Minimum temperature and maximum temperature
                                rightAxis.setAxisMinimum(5);
                                rightAxis.setAxisMaximum(45);
                                leftAxis.setAxisMinimum(0);
                                leftAxis.setAxisMaximum(10);
                                leftAxis.setDrawGridLines(true);
                                rightAxis.setDrawGridLines(false);
                                leftAxis.setGranularityEnabled(true);
                                rightAxis.setGranularityEnabled(true);

                                // Set the data and axis dependency (Left Y for pain scaling, Right Y for temperature scaling)
                                LineDataSet lineDataSet1 = new LineDataSet(pains, "Pain Level");
                                lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
                                lineDataSet1.setColor(Color.BLUE);
                                LineDataSet lineDataSet2 = new LineDataSet(temperatures, "Temperatures");
                                lineDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
                                lineDataSet2.setColor(Color.RED);

                                // Draw the line chart
                                LineData lineData = new LineData(lineDataSet1, lineDataSet2);
                                binding.lineChart.setData(lineData);
                                binding.lineChart.invalidate();
                                break;
                            }
                            case "Humidity": {
                                YAxis leftAxis = binding.lineChart.getAxisLeft();
                                YAxis rightAxis = binding.lineChart.getAxisRight();

                                rightAxis.setAxisMinimum(40);
                                rightAxis.setAxisMaximum(90);
                                leftAxis.setAxisMinimum(0);
                                leftAxis.setAxisMaximum(10);
                                leftAxis.setDrawGridLines(true);
                                rightAxis.setDrawGridLines(false);
                                leftAxis.setGranularityEnabled(true);
                                rightAxis.setGranularityEnabled(true);

                                LineDataSet lineDataSet1 = new LineDataSet(pains, "Pain Level");
                                lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
                                lineDataSet1.setColor(Color.BLUE);
                                LineDataSet lineDataSet2 = new LineDataSet(humidity, "Humidity");
                                LineData lineData = new LineData(lineDataSet1, lineDataSet2);
                                lineDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
                                lineDataSet2.setColor(Color.RED);

                                binding.lineChart.setData(lineData);
                                binding.lineChart.invalidate();
                                break;
                            }
                            case "Pressure": {
                                YAxis leftAxis = binding.lineChart.getAxisLeft();
                                YAxis rightAxis = binding.lineChart.getAxisRight();

                                rightAxis.setAxisMinimum(1000);
                                rightAxis.setAxisMaximum(1040);
                                leftAxis.setAxisMinimum(0);
                                leftAxis.setAxisMaximum(10);
                                leftAxis.setDrawGridLines(true);
                                rightAxis.setDrawGridLines(false);
                                leftAxis.setGranularityEnabled(true);
                                rightAxis.setGranularityEnabled(true);

                                LineDataSet lineDataSet1 = new LineDataSet(pains, "Pain Level");
                                lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
                                lineDataSet1.setColor(Color.BLUE);
                                LineDataSet lineDataSet2 = new LineDataSet(pressures, "Pressure");
                                LineData lineData = new LineData(lineDataSet1, lineDataSet2);
                                lineDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
                                lineDataSet2.setColor(Color.RED);

                                binding.lineChart.setData(lineData);
                                binding.lineChart.invalidate();
                                break;
                            }
                        }

                        binding.correlation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (userSelected.equals("Temperature")) {
                                    // Declare a new double [][]
                                    double [][] painTemperature = new double[0][0];
                                    // Looping over the size of painRecords (whatever that number will be)
                                    for (int i = 0; i < painRecords.size(); i++) {
                                        // The number of rows will equal to the number of records, column will be 2 (static)
                                        painTemperature = new double[painRecords.size()][2];
                                        // Add all values to this double [][] (use data for correlation that was defined before)
                                        for (int j = 0; j < painRecords.size(); j++) {
                                            painTemperature[j][0] = corPain.get(j);
                                            painTemperature[j][1] = corTemperature.get(j);
                                        }
                                    }

                                    // Calculate the correlation and p value
                                    RealMatrix matrix = MatrixUtils.createRealMatrix(painTemperature);
                                    PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
                                    RealMatrix corM = pc.getCorrelationMatrix();
                                    RealMatrix pM = pc.getCorrelationPValues();

                                    binding.rValue.setText("" + corM.getEntry(0,1));
                                    binding.pValue.setText("" + pM.getEntry(0, 1));


                                } else if (userSelected.equals("Humidity")) {
                                    double [][] painHumidity = new double[0][0];
                                    for (int i = 0; i < painRecords.size(); i++) {
                                        painHumidity = new double[painRecords.size()][2];
                                        for (int j = 0; j < painRecords.size(); j++) {
                                            painHumidity[j][0] = corPain.get(j);
                                            painHumidity[j][1] = corHumidity.get(j);
                                        }
                                    }

                                    RealMatrix matrix = MatrixUtils.createRealMatrix(painHumidity);
                                    PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
                                    RealMatrix corM = pc.getCorrelationMatrix();
                                    RealMatrix pM = pc.getCorrelationPValues();

                                    binding.rValue.setText("" + corM.getEntry(0,1));
                                    binding.pValue.setText("" + pM.getEntry(0, 1));


                                } else {
                                    double [][] painPressure = new double[0][0];
                                    for (int i = 0; i < painRecords.size(); i++) {
                                        painPressure = new double[painRecords.size()][2];
                                        for (int j = 0; j < painRecords.size(); j++) {
                                            painPressure[j][0] = corPain.get(j);
                                            painPressure[j][1] = corPressure.get(j);
                                        }
                                    }

                                    RealMatrix matrix = MatrixUtils.createRealMatrix(painPressure);
                                    PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
                                    RealMatrix corM = pc.getCorrelationMatrix();
                                    RealMatrix pM = pc.getCorrelationPValues();

                                    binding.rValue.setText("" + corM.getEntry(0,1));
                                    binding.pValue.setText("" + pM.getEntry(0, 1));


                                }
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        });


        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month + 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String yearString = Integer.toString(year);
        // Month starts from 0, so manually adding 1
        String monthString = Integer.toString(month + 1);
        String dayString = Integer.toString(dayOfMonth);

        // Manually formatting the date, and make sure that there is a 0 when the number is a single digit (for X axis use)
        if ((month + 1) < 10) {
            monthString = "0" + monthString;
        }

        if (dayOfMonth < 10) {
            dayString = "0" + dayString;
        }

        String date = yearString + "-" + monthString + "-" + dayString;

        SharedPreferences sharedPre = requireActivity().getSharedPreferences("Date", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPre.edit();
        // This startDate variable is used for identifying whether the date is for start date or end date
        if (startDate) {
            binding.startDate.setText(date);
            spEditor.putString("Start Date", date);
        } else {
            binding.endDate.setText(date);
            spEditor.putString("End Date", date);
        }
        spEditor.apply();
    }
}
