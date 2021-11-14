package team_10.example.coen390_ezcurtains;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        fragment.show(getSupportFragmentManager(), "insert_device");
        loadList();
    }

    // Load expandable list view
    public void loadList() {
        DatabaseHelper dbHelper = new DatabaseHelper((getBaseContext()));
        list_room_names = dbHelper.getRooms();
        list_devices = dbHelper.getDevices();
        adapter = new ExpandableListAdapter(HomeActivity.this, list_room_names, list_devices);
        expandableListView.setAdapter(adapter);
    }

    public void openSchedule(Device device, List<Schedule> list) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        // Pass device data
        Gson gson_device = new Gson();
        intent.putExtra("Device", gson_device.toJson(device));
        // Pass list data
        Gson gson_list = new Gson();
        intent.putExtra("List", gson_list.toJson(list));
        startActivity(intent);
    }
}
