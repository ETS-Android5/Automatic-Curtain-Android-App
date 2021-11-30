package team_10.example.coen390_ezcurtains;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;
import team_10.example.coen390_ezcurtains.Controllers.DatabaseHelper;
import team_10.example.coen390_ezcurtains.Models.Alarm;
import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Schedule;

public class SetScheduleFragment extends DialogFragment {
    protected TextView deviceName, roomName, openTime, closeTime;
    protected Button btn_setOpenTime, btn_setCloseTime, btn_save, btn_cancel;
    protected MaterialTimePicker timepickerOpen, timepickerClose;
    protected MaterialDayPicker dayPicker;
    protected Device device;

    public SetScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_schedule, container, false);
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        // Retrieve device data from schedule activity
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            device = gson.fromJson(bundle.getString("device"), Device.class);
        }
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

        deviceName.setText(device.getDeviceName());
        roomName.setText(device.getRoomName());

        btn_setOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerOpen.show(getChildFragmentManager(), "open_time_fragment");
                timepickerOpen.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Display time to open time textview
                        openTime.setText(timeString(timepickerOpen, timepickerOpen.getHour(), timepickerOpen.getMinute()));
                        openTime.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        btn_setCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerClose.show(getChildFragmentManager(), "close_time_fragment");
                timepickerClose.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Display time to close time textview
                        closeTime.setText(timeString(timepickerClose, timepickerClose.getHour(), timepickerClose.getMinute()));
                        closeTime.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dismiss(); }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                Calendar open = setCalendar(timepickerOpen.getHour(), timepickerOpen.getMinute());
                Calendar close = setCalendar(timepickerClose.getHour(), timepickerClose.getMinute());
                Schedule schedule = new Schedule();
                List<MaterialDayPicker.Weekday> list = dayPicker.getSelectedDays();
                schedule.setOpenTime(open.getTimeInMillis());
                schedule.setCloseTime(close.getTimeInMillis());
                schedule.setDeviceID(device.getDeviceID());
                schedule.setDaysOfTheWeek(fromWeekdayToInt(list));
                if (dbHelper.insertSchedule(schedule) != -1) {
                    ((ScheduleActivity)getActivity()).loadList();
                    createAlarm(schedule);
                    dismiss();
                    Toast.makeText(getActivity(), "Schedule saved successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Failed to add schedule", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    // Create alarms
    public void createAlarm(Schedule schedule) {
        Calendar alarmOpen = Calendar.getInstance();
        Calendar alarmClose = Calendar.getInstance();
        alarmOpen.setTimeInMillis(schedule.getOpenTime());
        alarmClose.setTimeInMillis(schedule.getCloseTime());
        List<Integer> days = schedule.getDaysOfTheWeek();
        for(int i=0; i<days.size();i++) {
            if(days.get(i) == 1) {
                alarmOpen.set(Calendar.DAY_OF_WEEK, i+1);
                alarmClose.set(Calendar.DAY_OF_WEEK, i+1);
                // check if alarm is being set in the past
                if(alarmOpen.getTimeInMillis() < System.currentTimeMillis())
                    alarmOpen.set(Calendar.DATE, 7);

                if(alarmClose.getTimeInMillis() < System.currentTimeMillis())
                    alarmClose.set(Calendar.DATE, 7);
            }

            final int id_open = (int) System.currentTimeMillis(); // id used to set multiple alarms
            final int id_close = (int) System.currentTimeMillis()+1; // id used to set multiple alarms
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), ScheduleReceiver.class);
            PendingIntent pendingIntentOpen = PendingIntent.getBroadcast(getContext(), id_open, intent, PendingIntent.FLAG_IMMUTABLE);
            PendingIntent pendingIntentClose = PendingIntent.getBroadcast(getContext(), id_close, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmOpen.getTimeInMillis(), 7*24*60*60*1000, pendingIntentOpen);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmClose.getTimeInMillis(), 7*24*60*60*1000, pendingIntentClose);
        }
    }

    // Delete alarm
    public void deleteAlarm(Alarm alarm) {

    }

    // Initialize timepickers
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

    // Format selected time into a string to be displayed in 12 hour format
    public String timeString(MaterialTimePicker view, int hour, int minute) {
        String min = null;
        String am_pm = null;
        String timeString = null;

        if (hour > 12) {
            hour -= 12;
            am_pm = "PM";
        }
        else if (hour == 0) {
            hour += 12;
            am_pm = "AM";
        }
        else if (hour == 12) { am_pm = "PM"; }
        else { am_pm = "AM"; }

        if (minute < 10) { min = "0" + minute; }
        else { min = String.valueOf(minute); }

        timeString = hour + " : " + min + " " + am_pm;
        return timeString;
    }

    // Create Calendar object for selected times
    public Calendar setCalendar(int hour, int min) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);
        return c;
    }

    // Create a list of weekdays where selected weekdays are 1 and not selected weekdays, 0
    public List<Integer> fromWeekdayToInt(List<MaterialDayPicker.Weekday> list) {
        List<Integer> daysOfTheWeek = new ArrayList<>(Collections.nCopies(7,0));
        for (MaterialDayPicker.Weekday weekday: list) {
            if (weekday == MaterialDayPicker.Weekday.MONDAY)
                daysOfTheWeek.set(0, 1);
            if (weekday == MaterialDayPicker.Weekday.TUESDAY)
                daysOfTheWeek.set(1, 1);
            if (weekday == MaterialDayPicker.Weekday.WEDNESDAY)
                daysOfTheWeek.set(2, 1);
            if (weekday == MaterialDayPicker.Weekday.THURSDAY)
                daysOfTheWeek.set(3, 1);
            if (weekday == MaterialDayPicker.Weekday.FRIDAY)
                daysOfTheWeek.set(4, 1);
            if (weekday == MaterialDayPicker.Weekday.SATURDAY)
                daysOfTheWeek.set(5, 1);
            if (weekday == MaterialDayPicker.Weekday.SUNDAY)
                daysOfTheWeek.set(6, 1);
        }
        return daysOfTheWeek;
    }
}