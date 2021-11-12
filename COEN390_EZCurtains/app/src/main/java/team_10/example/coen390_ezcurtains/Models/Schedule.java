package team_10.example.coen390_ezcurtains.Models;

import java.util.List;

public class Schedule {
    private int scheduleID;
    private int deviceID;
    private String openTime;
    private String closeTime;
    private List<Integer> daysOfTheWeek;

    public Schedule() {}

    public Schedule(int deviceID, String openTime, String closeTime, List<Integer> daysOfTheWeek) {
        this.deviceID = deviceID;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.daysOfTheWeek = daysOfTheWeek;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public List<Integer> getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public void setDaysOfTheWeek(List<Integer> daysOfTheWeek) {
        this.daysOfTheWeek = daysOfTheWeek;
    }
}
