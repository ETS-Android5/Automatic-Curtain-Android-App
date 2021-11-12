package team_10.example.coen390_ezcurtains.Models;

import java.util.List;

public class Device {
    private int deviceID;
    private String deviceName;
    private String roomName;

    public Device() {}

    public Device(String deviceName, String roomName) {
        this.deviceName = deviceName;
        this.roomName = roomName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getDeviceID() { return deviceID; }

    public void setDeviceID(int deviceID) { this.deviceID = deviceID; }
}


