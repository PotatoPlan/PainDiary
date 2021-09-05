package com.example.paindiaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.paindiaryapp.databinding.ActivityMainBinding;
import com.example.paindiaryapp.entity.PainRecord;
import com.example.paindiaryapp.viewmodel.PainRecordViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener {
    private ActivityMainBinding binding;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePageFragment()).commit();

        


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

// --------------------Initialise the drawer by find the id of the main activity xml (that's where the drawer is)--------------------
        drawer = findViewById(R.id.activity_main);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


// --------------------When the user clicked the logout button, sign the user out--------------------
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Authentication.class));
            }
        });

//        ---NOT LOADING HOME FRAGMENT NOW, BECAUSE IT CAN BE INCLUDED IN THE MAIN ACTIVITY ANYWAY---
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, new HomePageFragment()).commit();
//        }

// --------------------If there is no user logged in, a new intent will be created and used to start another activity, the Authentication.--------------------
//        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//            Intent intent = new Intent(this, Authentication.class);
//            startActivity(intent);
//            finish();
//        };
    }


    // --------------------Handle all click events (using switch) by giving different actions with different .getItemId()--------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePageFragment()).commit();
                break;
            case R.id.nav_painEntry:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PainEntryFragment()).commit();
                break;
            case R.id.nav_dailyRecord:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DailyRecordFragment()).commit();
                break;
            case R.id.nav_reports:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportsFragment()).commit();
                break;
            case R.id.nav_maps:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment()).commit();
                break;
        }

        // --------------------We will need to close this drawer once clicked (START means going back to left-hand side)--------------------
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // --------------------When clicking the BACK button, we do not want to close the activity, but just that drawer--------------------
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView reminderTime = findViewById(R.id.reminder_time);

        int remindHour = hourOfDay;
        int remindMinute = minute - 2;
        if (minute <= 1) {
            remindMinute = 58 + minute;
            remindHour--;
            if (hourOfDay == 0) {
                remindHour = 23;
            }
        }

//        String hour = Integer.toString(remindHour);
//        String min = Integer.toString(remindMinute);
//        Toast.makeText(this, hour + min, Toast.LENGTH_LONG).show();

        reminderTime.setText("Pain Diary will remind you at " + remindHour + ":" + remindMinute);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, remindHour);
        calendar.set(Calendar.MINUTE, remindMinute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}