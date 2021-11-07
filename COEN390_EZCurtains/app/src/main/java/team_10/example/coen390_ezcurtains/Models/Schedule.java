package team_10.example.coen390_ezcurtains.Models;

import java.sql.Time;

public class Schedule {
    private int scheduleID;
    private String openTime;
    private String closeTime;

    public Schedule() {}

    public Schedule(int scheduleID, String openTime, String closeTime) {
        this.scheduleID = scheduleID;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
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
}
