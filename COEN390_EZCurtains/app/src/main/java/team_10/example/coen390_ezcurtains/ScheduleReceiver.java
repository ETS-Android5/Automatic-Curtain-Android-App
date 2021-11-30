package team_10.example.coen390_ezcurtains;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScheduleReceiver extends BroadcastReceiver {
    public boolean open = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference test = database.getReference("Motor_start");
        if(open){
            test.setValue(1);
            Toast.makeText(context, "Opening", Toast.LENGTH_SHORT).show();
            // run motor for 5 seconds
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    test.setValue(0);
                }
            }, 5000); // change value to adjust time
            open = false;
        }
        else {
            test.setValue(-1);
            Toast.makeText(context, "Close", Toast.LENGTH_SHORT).show();
            // run motor for 5 seconds
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    test.setValue(0);
                }
            }, 5000); // change value to adjust time
            open = true;
        }


    }
}
