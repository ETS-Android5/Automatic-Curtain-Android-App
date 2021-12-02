package team_10.example.coen390_ezcurtains;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import team_10.example.coen390_ezcurtains.Controllers.DatabaseHelper;
import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Schedule;

public class ScheduleActivity extends AppCompatActivity {
    protected FloatingActionButton btn_add_schedule;
    protected TextView sensorValue;
    protected ListView listView;
    protected List<Schedule> scheduleList;
    protected ListAdapter adapter;
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
        sensorValue = findViewById(R.id.sensor_value);
        listView = findViewById(R.id.list_schedule);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lightSensor");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(Integer.class);
                if(value <= 100)
                    sensorValue.setText("High");
                else if(value >= 800)
                    sensorValue.setText("Low");
                else
                    sensorValue.setText("Medium");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });

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
                registerForContextMenu(listView);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Schedule schedule = (Schedule) scheduleList.get(menuInfo.position);
        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        if(item.getItemId() == R.id.edit) {
            SetScheduleFragment fragment = new SetScheduleFragment();
            // Pass device data to set schedule fragment
            Gson gson_device = new Gson();
            Gson gson_schedule = new Gson();
            Bundle bundle = new Bundle();
            bundle.putString("device", gson_device.toJson(device));
            bundle.putString("schedule", gson_schedule.toJson(schedule));
            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(), "edit_schedule");
            return true;
        }
        else if(item.getItemId() == R.id.delete) {
            if(dbHelper.removeSchedule(schedule.getScheduleID()) != -1)
                Toast.makeText(this, "Successfully deleted schedule", Toast.LENGTH_SHORT).show();
            loadList();
            return true;
        }
        else
            return super.onContextItemSelected(item);
    }

    public void loadList() {
        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        scheduleList = dbHelper.getSchedule(device.getDeviceID());
        adapter = new ListAdapter(this, scheduleList);
        listView.setAdapter(adapter);
    }

    // Create alarms
    public void createAlarm(List<Schedule> scheduleList) {
        Calendar alarmOpen = Calendar.getInstance();
        Calendar alarmClose = Calendar.getInstance();
        for (Schedule schedule:scheduleList) {
            alarmOpen.setTimeInMillis(schedule.getOpenTime());
            alarmClose.setTimeInMillis(schedule.getCloseTime());
            List<Integer> days = schedule.getDaysOfTheWeek();
            for(int i=1; i<=days.size();i++) {
                if(days.get(i) == 1) {
                    alarmOpen.set(Calendar.DAY_OF_WEEK, i);
                    // check if alarm is being set in the past
                    if(alarmOpen.getTimeInMillis() < System.currentTimeMillis())
                        alarmOpen.set(Calendar.DATE, 7);
                }

                final int id = (int) System.currentTimeMillis(); // id used to set multiple alarms
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, ScheduleReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmOpen.getTimeInMillis(), 7*24*60*60*1000, pendingIntent);
            }
        }
    }
}