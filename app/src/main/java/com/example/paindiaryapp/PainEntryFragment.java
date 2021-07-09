package com.example.paindiaryapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paindiaryapp.databinding.FragmentPainEntryBinding;
import com.example.paindiaryapp.entity.PainRecord;
import com.example.paindiaryapp.viewmodel.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PainEntryFragment extends Fragment {
    private FragmentPainEntryBinding binding;
    private PainRecordViewModel painRecordViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPainEntryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Populating the ViewModel
        painRecordViewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);

        // Call the method to make the lists
        setUpDropDownMenu();


// --------------------Validate if there is an existing data in the database, if yes, disable user's input--------------------
        // Get current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // Get date that is already saved in the database (by using SharedPreference)
        SharedPreferences sharedPre = requireActivity().getSharedPreferences("Date", Context.MODE_PRIVATE);
        String savedDate = sharedPre.getString("date", null);
        if (currentDate.equals(savedDate)) {
            disableEdit();
        }

// --------------------Update the Goal Step UI with user's actual input (by using SharedPreference, too)--------------------
        String userGoalSteps = sharedPre.getString("goalSteps", "10000");
        if (!userGoalSteps.isEmpty()) {
            binding.goalSteps.setText(userGoalSteps);
        }





        binding.reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePicker();
                timePicker.show(getActivity().getSupportFragmentManager(), "time picker");
            }
        });




// --------------------OnClickListener on the SAVE button, user should not be able to edit the input--------------------
        binding.entrySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user's email, current date, and all weather information (obtained from a public API)
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                // Pass this date to the SharedPreference for later validation
                SharedPreferences.Editor spEditor = sharedPre.edit();
                spEditor.putString("date", date);
                spEditor.apply();



                // Got weather data from HomePageFragment by using SharedPreferences
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Weather", Context.MODE_PRIVATE);
                String temperature = sharedPreferences.getString("Temperature", null);
                String humidity = sharedPreferences.getString("Humidity", null);
                String pressure = sharedPreferences.getString("Pressure", null);



                // Get Pain Intensity Level, Steps Taken, Pain Location and Mood Level
                int pil = (int) binding.painSlider.getValue();
                String painIntensityLevel = Integer.toString(pil);

                String todaySteps = binding.todaySteps.getText().toString();

                // Get goal steps and put it into a SharedPreference
                String goalSteps = binding.goalSteps.getText().toString();
                spEditor.putString("goalSteps", goalSteps);
                spEditor.apply();

                String painLocation = binding.materialTextInput.getEditText().getText().toString();

                String moodLevel = binding.materialTextInput2.getEditText().getText().toString();

                // Write data into the database
                if (!painLocation.equals("Please choose one") && !moodLevel.equals("Please choose one") && !todaySteps.isEmpty()) {
                    PainRecord painRecord = new PainRecord(date, painIntensityLevel, painLocation, moodLevel, todaySteps, goalSteps, temperature, humidity, pressure, userEmail);
                    painRecordViewModel.insert(painRecord);
                    disableEdit();
                    Toast.makeText(getActivity(), "Data Saved", Toast.LENGTH_LONG).show();
                } else if (painLocation.equals("Please choose one")) {
                    Toast.makeText(getActivity(), "Please Choose Your Location of Pain", Toast.LENGTH_LONG).show();
                    allowEdit();
                } else if (moodLevel.equals("Please choose one")) {
                    Toast.makeText(getActivity(), "Please Choose Your Mood", Toast.LENGTH_LONG).show();
                    allowEdit();
                } else {
                    Toast.makeText(getActivity(), "Please Enter Your Today's Step", Toast.LENGTH_LONG).show();
                    allowEdit();
                }

            }
        });


// --------------------OnClickListener on the EDIT button, user should be able to edit the input--------------------
        binding.entryEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowEdit();
            }
        });


        return view;
    }



    private void allowEdit() {
        binding.painSlider.setEnabled(true);
        binding.goalSteps.setEnabled(true);
        binding.todaySteps.setEnabled(true);
        binding.locationOfPain.setEnabled(true);
        binding.mood.setEnabled(true);
        binding.entrySave.setEnabled(true);
    }

    private void disableEdit() {
        binding.painSlider.setEnabled(false);
        binding.goalSteps.setEnabled(false);
        binding.todaySteps.setEnabled(false);
        binding.locationOfPain.setEnabled(false);
        binding.mood.setEnabled(false);
        binding.entrySave.setEnabled(false);
    }

    private void setUpDropDownMenu() {
        ArrayList<String> locationList = new ArrayList<>();
        locationList.add("back");
        locationList.add("neck");
        locationList.add("head");
        locationList.add("knees");
        locationList.add("hips");
        locationList.add("abdomen");
        locationList.add("elbows");
        locationList.add("shoulders");
        locationList.add("shins");
        locationList.add("jaw");
        locationList.add("facial");

        ArrayList<String> moodList = new ArrayList<>();
        moodList.add("very low");
        moodList.add("low");
        moodList.add("average");
        moodList.add("good");
        moodList.add("very good");

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(getContext(), R.layout.location_of_pain_item, locationList);
        binding.locationOfPain.setAdapter(locationAdapter);

        ArrayAdapter<String> moodAdapter = new ArrayAdapter<>(getContext(), R.layout.mood_item, moodList);
        binding.mood.setAdapter(moodAdapter);
    }

    @Override
    public void onResume() {
        // --------------------Call the method again to make sure it can re-appear--------------------
        setUpDropDownMenu();
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
