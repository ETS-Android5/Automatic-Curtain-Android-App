package team_10.example.coen390_ezcurtains;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import team_10.example.coen390_ezcurtains.Controllers.DatabaseHelper;
import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Room;

public class InsertDeviceFragment extends DialogFragment {
    protected EditText roomName, deviceName;
    protected Button btn_save, btn_cancel;

    public InsertDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert_device, container, false);
        deviceName = view.findViewById(R.id.enter_deviceName);
        roomName = view.findViewById(R.id.enter_roomName);
        btn_save = view.findViewById(R.id.btn_save);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dismiss(); }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = deviceName.getText().toString();
                String room = roomName.getText().toString();
                int scheduleID = 0;

                if (!(name.equals("") || room.equals(""))) {
                    // Save in db
                    dbHelper.insertDevice(new Device(name, room));
                    dbHelper.insertRoom(new Room(room));
                    ((HomeActivity)getActivity()).loadList();
                    dismiss();
                    Toast.makeText(getActivity(), "Device saved successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
