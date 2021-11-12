package team_10.example.coen390_ezcurtains;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import ca.antonious.materialdaypicker.MaterialDayPicker;
import team_10.example.coen390_ezcurtains.Controllers.DatabaseHelper;

public class SetScheduleFragment extends DialogFragment {
    protected TextView deviceName, roomName, openTime, closeTime;
    protected Button btn_setOpenTime, btn_setCloseTime, btn_save, btn_cancel;
    protected MaterialTimePicker timepickerOpen, timepickerClose;
    protected MaterialDayPicker dayPicker;

    public SetScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_schedule, container, false);
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        createTimePickers();
        deviceName = view.findViewById(R.id.text_device_name);
        roomName = view.findViewById(R.id.text_room_name);
        openTime = view.findViewById(R.id.text_open_time);
        closeTime = view.findViewById(R.id.text_close_time);
        btn_setOpenTime = view.findViewById(R.id.set_open_time);
        btn_setCloseTime = view.findViewById(R.id.set_close_time);
        btn_save = view.findViewById(R.id.btn_save);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        dayPicker = view.findViewById(R.id.day_picker);

        btn_setCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerClose.show(getChildFragmentManager(), "fragment_tag");

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dismiss(); }
        });



        return view;
    }

    public void createTimePickers() {
        timepickerOpen = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Set Open Time")
                .setHour(12)
                .setMinute(0)
                .build();

        timepickerClose = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Set Close Time")
                .setHour(12)
                .setMinute(0)
                .build();
    }
}
