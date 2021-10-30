package team_10.example.coen390_ezcurtains;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

public class ScheduleActivity extends AppCompatActivity {
    protected TimePicker time_picker;
    protected TextView toolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar_text = findViewById(R.id.text_toolbar_name);
        toolbar_text.setText("Schedule");
        time_picker = findViewById(R.id.time_picker);
        time_picker.setIs24HourView(true);

    }
}