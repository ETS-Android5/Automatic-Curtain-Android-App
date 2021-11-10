package team_10.example.coen390_ezcurtains.Models;

public class Device {
    private int scheduleID;
    private String deviceName;
    private String roomName;

    public Device() {}

    public Device(int scheduleID, String deviceName, String roomName) {
        this.scheduleID = scheduleID;
        this.deviceName = deviceName;
        this.roomName = roomName;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
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
}


