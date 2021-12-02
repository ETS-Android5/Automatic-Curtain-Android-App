package team_10.example.coen390_ezcurtains;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(Integer.class);
                DatabaseHelper dbHelper = new DatabaseHelper((getBaseContext()));
                if (value < 200 && !dbHelper.checkSelectedChild(0,0)) {
                    expandableListView.collapseGroup(0);
                    motor.setValue(1);
                    // run motor for 6 seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            motor.setValue(0);
                            dbHelper.insertSelectedChild(0,0);
                        }
                    }, 6000); // change value to adjust time

                }
                else if (value >= 700 && dbHelper.checkSelectedChild(0,0)) {
                    motor.setValue(-1);
                    expandableListView.collapseGroup(0);
                    // run motor for 6 seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            motor.setValue(0);
                            dbHelper.removeSelectedChild(0,0);
                        }
                    }, 6000); // change value to adjust time

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

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                registerForContextMenu(expandableListView);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.device_room_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();
        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        if(item.getItemId() == R.id.delete) {
            if (ExpandableListView.getPackedPositionType(info.packedPosition) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                long id = -1;
                int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
                int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
                Device device = list_devices.get(list_room_names.get(groupPos).getRoomName()).get(childPos);
                List<Device> deviceList = list_devices.get(list_room_names.get(groupPos).getRoomName());
                if (deviceList.size() == 1) {
                    dbHelper.removeRoom(device.getRoomName(), deviceList);
                }
                else if (!dbHelper.removeDevice(device.getDeviceID()).contains(id)) {
                    Toast.makeText(this, "Successfully deleted device", Toast.LENGTH_SHORT).show();
                }
            }
            else if (ExpandableListView.getPackedPositionType(info.packedPosition) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                Room room = list_room_names.get(ExpandableListView.getPackedPositionGroup(info.packedPosition));
                dbHelper.removeRoom(room.getRoomName(), list_devices.get(room.getRoomName()));
            }
            loadList();
            return true;
        }
        else
            return super.onContextItemSelected(item);
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
