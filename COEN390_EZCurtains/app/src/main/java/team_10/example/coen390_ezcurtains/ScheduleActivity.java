package team_10.example.coen390_ezcurtains;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.List;

import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Schedule;

public class ScheduleActivity extends AppCompatActivity {
    protected Button setOpenTime, setCloseTime;
    protected TextView txt_openTime, txt_closeTime;
    protected MaterialTimePicker timePickerOpen, timePickerClose;
    protected List<Schedule> scheduleList;
    protected Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);
        // Retrieve device and schedule list from home activity
        Intent i = getIntent();
        device = (Device) i.getSerializableExtra("List");
        scheduleList = (ArrayList<Schedule>) i.getSerializableExtra("Device");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createTimePickers();

        setOpenTime = findViewById(R.id.set_open_time);
        setCloseTime = findViewById(R.id.set_close_time);
        txt_closeTime = findViewById(R.id.text_close_time);
        txt_openTime = findViewById(R.id.text_open_time);

        setOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerOpen.show(getSupportFragmentManager(), "fragment_tag");
                timePickerOpen.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txt_openTime.setText(timeString(timePickerOpen, timePickerOpen.getHour(), timePickerOpen.getMinute()));
                        txt_openTime.setVisibility(View.VISIBLE);
                        // Save open time to device attributes
                    }
                });
            }
        });

        setCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerClose.show(getSupportFragmentManager(), "fragment_tag");
                timePickerClose.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txt_closeTime.setText(timeString(timePickerClose, timePickerClose.getHour(), timePickerClose.getMinute()));
                        txt_closeTime.setVisibility(View.VISIBLE);
                        // Save close time to device attributes
                    }
                });
            }
        });



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedule_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void createTimePickers() {
        timePickerOpen = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Set Open Time")
                .setHour(12)
                .setMinute(0)
                .build();

        timePickerClose = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Set Close Time")
                .setHour(12)
                .setMinute(0)
                .build();
    }

    public String timeString(MaterialTimePicker view, int hour, int minute) {
        String min = null;
        String am_pm = null;
        String timeString = null;

        if (hour > 12) {
            hour -= 12;
            am_pm = "PM";
        }
        else if (hour == 0) {
            hour += 12;
            am_pm = "AM";
        }
        else if (hour == 12) { am_pm = "PM"; }
        else { am_pm = "AM"; }

        if (minute < 10) { min = "0" + minute; }
        else { min = String.valueOf(minute); }

        timeString = hour + " : " + min + " " + am_pm;
        return timeString;
    }
}