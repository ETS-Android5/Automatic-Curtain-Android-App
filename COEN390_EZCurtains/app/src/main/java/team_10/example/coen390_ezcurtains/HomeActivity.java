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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import team_10.example.coen390_ezcurtains.Controllers.DatabaseHelper;
import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Room;

public class HomeActivity extends AppCompatActivity {
    protected ExpandableListView expandableListView;
    protected ExpandableListAdapter adapter;
    protected List<Room> list_room_names;
    protected HashMap<String, List<Device>> list_devices;
    protected Button btn_addRoom, btn_addDevice;

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
        btn_addRoom = findViewById(R.id.btn_add_room);
        btn_addDevice = findViewById(R.id.btn_add_device);
        //loadList();

        // is add room button really necessary?
        btn_addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open add room dialog and save room name to list_room_names
            }
        });

        btn_addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open add device dialog and save device to list_devices
                addDevice();
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
        list_room_names = dbHelper.getRooms();
        list_devices = dbHelper.getDevices();
        adapter = new ExpandableListAdapter(HomeActivity.this, list_room_names, list_devices);
        expandableListView.setAdapter(adapter);
    }

    public void openSchedule() {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);

    }
}
