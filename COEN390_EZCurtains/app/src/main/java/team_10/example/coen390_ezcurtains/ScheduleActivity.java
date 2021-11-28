package team_10.example.coen390_ezcurtains;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import team_10.example.coen390_ezcurtains.Controllers.DatabaseHelper;
import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Schedule;

public class ScheduleActivity extends AppCompatActivity {
    protected FloatingActionButton btn_add_schedule;
    protected ListView listView;
    protected List<Schedule> scheduleList;
    protected ArrayAdapter adapter;
    protected Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_add_schedule = findViewById(R.id.btn_add_schedule);
        listView = findViewById(R.id.list_schedule);

        //loadList();

        btn_add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetScheduleFragment fragment = new SetScheduleFragment();
                // Pass device data to set schedule fragment
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString("device", gson.toJson(device));
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "set_schedule");
                loadList();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Open menu with delete options
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get intent from home activity
        Intent data = getIntent();

        // Retrieve device data from json string
        Gson gson_device = new Gson();
        device = gson_device.fromJson(data.getStringExtra("Device"), Device.class);

        // Retrieve schedule list from json string
        Gson gson_list = new Gson();
        Type type = new TypeToken<ArrayList<Schedule>>() {}.getType();
        if (gson_list.fromJson(data.getStringExtra("List"), type) == null) {
            scheduleList = new ArrayList<>(); // Avoid null pointer exception
        }
        else
            scheduleList = gson_list.fromJson(data.getStringExtra("List"), type);
        loadList();
    }

    public void loadList() {
        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        scheduleList = dbHelper.getSchedule(device.getDeviceID());
        List<String> displayedList = new ArrayList<>();
        String log;
        // Format string to display in list view
        for (Schedule schedule : scheduleList) {
            List<Integer> list_days = schedule.getDaysOfTheWeek();
            String day_string = null;
            for (int i = 0; i < list_days.size(); i++) {
                int day = list_days.get(i);
                switch (i) {
                    case 0:
                        if (day == 1 && day_string == null) {
                            day_string = "Mon. ";
                            break;
                        }
                    case 1:
                        if (day == 1 && day_string == null) {
                            day_string = "Tue. ";
                            break;
                        }
                        else if (day == 1){
                            day_string = day_string+" Tue. ";
                            break;
                        }
                    case 2:
                        if (day == 1 && day_string == null) {
                            day_string = "Wed. ";
                            break;
                        }
                        else if (day == 1) {
                            day_string = day_string+" Wed. ";
                            break;
                        }
                    case 3:
                        if (day == 1 && day_string == null) {
                            day_string = "Thu. ";
                            break;
                        }
                        else if (day == 1) {
                            day_string = day_string+" Thu. ";
                            break;
                        }
                    case 4:
                        if (day == 1 && day_string == null) {
                            day_string = "Fri. ";
                            break;
                        }
                        else if (day == 1) {
                            day_string = day_string+" Fri. ";
                            break;
                        }
                    case 5:
                        if (day == 1 && day_string == null) {
                            day_string = "Sat. ";
                            break;
                        }
                        else if (day == 1){
                            day_string = day_string+" Sat. ";
                            break;
                        }
                    case 6:
                        if (day == 1 && day_string == null) {
                            day_string = "Sun. ";
                            break;
                        }
                        else if (day == 1){
                            day_string = day_string+" Sun.";
                            break;
                        }
                }
            }
            log = "Schedule "+schedule.getScheduleID()+"\nOpen Time: "+schedule.getOpenTime()+"        Close Time: "+schedule.getCloseTime()+"\n"+day_string;
            displayedList.add(log);
        }
        adapter = new ArrayAdapter(this, R.layout.schedule_listview_layout, displayedList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}