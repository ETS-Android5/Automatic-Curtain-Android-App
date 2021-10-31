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

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> list_header; // room names
    private HashMap<String, List<String>> list_child; // device names

    public ExpandableListAdapter(Context context, List<String> list_header, HashMap<String, List<String>> list_child) {
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
        return this.list_child.get(this.list_header.get(i)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.list_header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.list_child.get(this.list_header.get(groupPosition)).get(childPosition);
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
        String headerTitle = (String) getGroup(headerPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_layout, null);
        }

        TextView text_header = (TextView) view.findViewById(R.id.text_room_name);
        text_header.setTypeface(null, Typeface.BOLD);
        text_header.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int headerPosition, final int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        String childText = (String) getChild(headerPosition, childPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.subitem_layout, null);
        }

        TextView text_child = (TextView) view.findViewById(R.id.text_device_name);
        TextView add_schedule = (TextView) view.findViewById(R.id.text_add_schedule);
        text_child.setText(childText);

        add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScheduleActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
