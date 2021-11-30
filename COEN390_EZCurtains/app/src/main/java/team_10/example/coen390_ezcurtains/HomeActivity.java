package team_10.example.coen390_ezcurtains;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import team_10.example.coen390_ezcurtains.Controllers.DatabaseHelper;
import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Room;
import team_10.example.coen390_ezcurtains.Models.Schedule;


public class HomeActivity extends AppCompatActivity {
    protected ExpandableListView expandableListView;
    protected ExpandableListAdapter adapter;
    protected List<Room> list_room_names;
    protected HashMap<String, List<Device>> list_devices;
    protected Button btn_addDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_layout);
        list_room_names = new ArrayList<>();
        list_devices = new HashMap<>();

        // Initialize toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Devices");

        expandableListView = findViewById(R.id.list);
        btn_addDevice = findViewById(R.id.btn_add_device);
        loadList();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lightSensor");
        DatabaseReference motor = database.getReference("Motor_start");
        //myRef.setValue("Upload Success!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            boolean open = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(Integer.class);
                //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                if (value < 100 && open) {
                    motor.setValue(1);
                    // run motor for 5 seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            motor.setValue(0);
                        }
                    }, 5000); // change value to adjust time
                    open = false;
                }
                else if (value > 800 && !open) {
                    motor.setValue(-1);
                    // run motor for 5 seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            motor.setValue(0);
                        }
                    }, 5000); // change value to adjust time
                    open = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });

        btn_addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open add device dialog and save device to list_devices
                addDevice();
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                DatabaseHelper db = new DatabaseHelper((getBaseContext()));
                Device device = adapter.getChild(i, i1);
                List<Schedule> scheduleList = db.getSchedule(device.getDeviceID());
                openSchedule(device, scheduleList);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    // Add device
    public void addDevice() {
        InsertDeviceFragment fragment = new InsertDeviceFragment();
        fragment.show(getSupportFragmentManager(), "Insert Device");
        loadList();
    }

    // Load expandable list view
    public void loadList() {
        DatabaseHelper dbHelper = new DatabaseHelper((getBaseContext()));
        Parcelable state = expandableListView.onSaveInstanceState();
        list_room_names = dbHelper.getRooms();
        list_devices = dbHelper.getDevices();
        adapter = new ExpandableListAdapter(HomeActivity.this, list_room_names, list_devices);
        expandableListView.setAdapter(adapter);
        expandableListView.onRestoreInstanceState(state);
    }

    public void openSchedule(Device device, List<Schedule> list) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        // Pass device data
        Gson gson1 = new Gson();
        intent.putExtra("Device", gson1.toJson(device));
        // Check for empty list
        Gson gson2 = new Gson();
        intent.putExtra("List", gson2.toJson(list));
        startActivity(intent);
    }
}
