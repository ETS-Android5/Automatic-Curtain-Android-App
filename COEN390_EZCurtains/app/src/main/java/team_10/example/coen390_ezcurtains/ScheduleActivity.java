package team_10.example.coen390_ezcurtains;

import android.os.Bundle;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class ScheduleActivity extends AppCompatActivity {
    protected TimePicker time_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);

        time_picker = findViewById(R.id.time_picker);
        time_picker.setIs24HourView(true);
    }
}