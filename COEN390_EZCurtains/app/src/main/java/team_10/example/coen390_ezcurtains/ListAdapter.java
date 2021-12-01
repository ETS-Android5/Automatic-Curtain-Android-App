package team_10.example.coen390_ezcurtains;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import team_10.example.coen390_ezcurtains.Models.Schedule;

public class ListAdapter extends ArrayAdapter<Schedule> {
    private Context context;
    private List<Schedule> scheduleList = new ArrayList<Schedule>();

    public ListAdapter(@NonNull Context context, List<Schedule> list) {
        super(context, 0, list);
        this.context = context;
        this.scheduleList = list;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.schedule_listview_layout, parent, false);

        Schedule schedule = scheduleList.get(pos);
        TextView open_time = listItem.findViewById(R.id.open_time);
        TextView close_time = listItem.findViewById(R.id.close_time);
        TextView repeating = listItem.findViewById(R.id.repeating_days);
        open_time.setText(timeString(schedule.getOpenTime()));
        close_time.setText(timeString(schedule.getCloseTime()));
        repeating.setText(repeatingDays(schedule.getDaysOfTheWeek()));

        return listItem;
    }

    // Format selected time into a string to be displayed in 12 hour format
    public String timeString(long timeInMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String min = null;
        String am_pm = null;
        String timeString = null;

        if (hour > 12) {
            hour -= 12;
            am_pm = "PM";
        } else if (hour == 0) {
            hour += 12;
            am_pm = "AM";
        } else if (hour == 12) {
            am_pm = "PM";
        } else {
            am_pm = "AM";
        }

        if (minute < 10) {
            min = "0" + minute;
        } else {
            min = String.valueOf(minute);
        }

        timeString = hour + ":" + min + " " + am_pm;
        return timeString;
    }

    public String repeatingDays(List<Integer> days) {
        String day_string = "";
        for (int i = 0; i < days.size(); i++) {
            int day = days.get(i);
            switch (i) {
                case 0:
                    if (day == 1) {
                        day_string = "Mon. ";
                        break;
                    }
                case 1:
                    if (day == 1 && day_string.equals("")) {
                        day_string = "Tue. ";
                        break;
                    } else if (day == 1) {
                        day_string = day_string + " Tue.";
                        break;
                    }
                case 2:
                    if (day == 1 && day_string.equals("")) {
                        day_string = "Wed. ";
                        break;
                    } else if (day == 1) {
                        day_string = day_string + " Wed. ";
                        break;
                    }
                case 3:
                    if (day == 1 && day_string.equals("")) {
                        day_string = "Thu. ";
                        break;
                    } else if (day == 1) {
                        day_string = day_string + " Thu. ";
                        break;
                    }
                case 4:
                    if (day == 1 && day_string.equals("")) {
                        day_string = "Fri. ";
                        break;
                    } else if (day == 1) {
                        day_string = day_string + " Fri. ";
                        break;
                    }
                case 5:
                    if (day == 1 && day_string.equals("")) {
                        day_string = "Sat. ";
                        break;
                    } else if (day == 1) {
                        day_string = day_string + " Sat. ";
                        break;
                    }
                case 6:
                    if (day == 1 && day_string.equals("")) {
                        day_string = "Sun. ";
                        break;
                    } else if (day == 1) {
                        day_string = day_string + " Sun. ";
                        break;
                    }
            }
        }
        if(day_string.equals(""))
            day_string = "Not scheduled";
        return day_string;
    }
}
