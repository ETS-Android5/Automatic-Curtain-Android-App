package team_10.example.coen390_ezcurtains;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Room;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Room> list_header; // rooms
    private HashMap<String, List<Device>> list_child; // device

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
        text_header.setTypeface(null, Typeface.BOLD);
        text_header.setText(header.getRoomName());
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
        TextView add_schedule = view.findViewById(R.id.text_add_schedule);
        text_child.setText(child.getDeviceName());

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
