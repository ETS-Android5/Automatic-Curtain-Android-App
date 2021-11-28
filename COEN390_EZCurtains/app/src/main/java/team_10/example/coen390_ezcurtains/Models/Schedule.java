package team_10.example.coen390_ezcurtains.Models;

import java.util.List;

public class Schedule {
    private int scheduleID;
    private int deviceID;
    private long openTime;
    private long closeTime;
    private List<Integer> daysOfTheWeek;

    public Schedule() {}

    public Schedule(int deviceID, long openTime, long closeTime, List<Integer> daysOfTheWeek) {
        this.deviceID = deviceID;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.daysOfTheWeek = daysOfTheWeek;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) { this.openTime = openTime; }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
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
