package team_10.example.coen390_ezcurtains;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import team_10.example.coen390_ezcurtains.Controllers.DatabaseHelper;
import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Room;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Room> list_header; // rooms
    private HashMap<String, List<Device>> list_child; // device
    private HashMap<Integer, Integer> pressed_items; // pressed child items
    private List<Integer> pressed_header_items; // pressed parent items

    public ExpandableListAdapter(Context context, List<Room> list_header, HashMap<String, List<Device>> list_child) {
        this.context = context;
        this.list_child = list_child;
        this.list_header = list_header;
    }

    @Override
    public int getGroupCount() {
        return this.list_header.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.list_child.get(this.list_header.get(i).getRoomName()).size();
    }

    @Override
    public Room getGroup(int groupPosition) {
        return this.list_header.get(groupPosition);
    }

    @Override
    public Device getChild(int groupPosition, int childPosition) {
        return this.list_child.get(this.list_header.get(groupPosition).getRoomName()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int headerPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        Room header = getGroup(headerPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_layout, null);
        }

        TextView text_header = view.findViewById(R.id.text_room_name);
        Button btnOpenCloseAll = view.findViewById(R.id.btn_open_close_all);
        DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
        text_header.setTypeface(null, Typeface.BOLD);
        text_header.setText(header.getRoomName());
        // Format button text
        if (!dbHelper.checkSelectedParent(headerPosition)) {
            btnOpenCloseAll.setText("Open All");
        }
        else {
            btnOpenCloseAll.setText("Close All");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference test = database.getReference("Motor_start");


        btnOpenCloseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // OPEN
                if (!dbHelper.checkSelectedParent(headerPosition)) {
                    btnOpenCloseAll.setText("Close All");
                    dbHelper.insertSelectedParent(headerPosition);
                    test.setValue(1);
                    // run motor for 5 seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            test.setValue(0);
                        }
                    }, 5000); // change value to adjust time
                }
                // CLOSE
                else {
                    btnOpenCloseAll.setText("Open All");
                    dbHelper.removeSelectedParent(headerPosition);
                    test.setValue(-1);
                    // run motor for 5 seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            test.setValue(0);
                        }
                    }, 5000); // change value to adjust time
                }
            }
        });
        return view;
    }

    @Override
    public View getChildView(int headerPosition, final int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        Device child = getChild(headerPosition, childPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.subitem_layout, null);
        }

        TextView text_child = view.findViewById(R.id.text_device_name);
        Button btnOpenCloseDevice = view.findViewById(R.id.btn_open_close);
        DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
        text_child.setText(child.getDeviceName());

        // Format button text
        if (!dbHelper.checkSelectedChild(headerPosition, childPosition)) {
            btnOpenCloseDevice.setText("Open");
        }
        else {
            btnOpenCloseDevice.setText("Close");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference test = database.getReference("Motor_start");

        btnOpenCloseDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // OPEN
                if (!dbHelper.checkSelectedChild(headerPosition, childPosition)) {
                    btnOpenCloseDevice.setText("Close");
                    dbHelper.insertSelectedChild(headerPosition, childPosition);
                    test.setValue(1);
                    // run motor for 5 seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            test.setValue(0);
                        }
                    }, 5000); // change value to adjust time
                }
                // CLOSE
                else {
                    btnOpenCloseDevice.setText("Open");
                    dbHelper.removeSelectedChild(headerPosition, childPosition);
                    test.setValue(-1);
                    // run motor for 5 seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            test.setValue(0);
                        }
                    }, 5000); // change value to adjust time
                }
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
