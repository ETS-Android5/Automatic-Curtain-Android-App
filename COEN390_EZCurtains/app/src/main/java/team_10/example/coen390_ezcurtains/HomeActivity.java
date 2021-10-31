package team_10.example.coen390_ezcurtains;

import android.content.Intent;
import android.os.Bundle;
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

public class HomeActivity extends AppCompatActivity {
    protected ExpandableListView expandableListView;
    protected ExpandableListAdapter adapter;
    protected List<String> list_room_names;
    protected HashMap<String, List<String>> list_devices;
    protected Button btn_addRoom, btn_addDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_layout);
        list_room_names = new ArrayList<String>();
        list_devices = new HashMap<String, List<String>>();

        // Initialize toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Devices");

        loadList();
        expandableListView = findViewById(R.id.list);
        adapter = new ExpandableListAdapter(HomeActivity.this, list_room_names, list_devices);

        expandableListView.setAdapter(adapter);
        btn_addRoom = findViewById(R.id.btn_add_room);
        btn_addDevice = findViewById(R.id.btn_add_device);


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

//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
//                return false;
//            }
//        });

    }

    // Add device
    private void addDevice() {
    }

    // Load expandable list view
    private void loadList() {
        list_room_names.add("Living Room");
        List<String> living_room = new ArrayList<>();
        living_room.add("Left Window");
        living_room.add("Right Window");
        list_devices.put(list_room_names.get(0), living_room);
    }

    protected void openSchedule() {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);

    }
}
