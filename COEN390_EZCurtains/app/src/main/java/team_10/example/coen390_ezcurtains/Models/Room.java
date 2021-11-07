package team_10.example.coen390_ezcurtains.Models;

public class Room {
    private int rID;
    private String roomName;

    public Room(int rID, String roomName) {
        this.rID = rID;
        this.roomName = roomName;
    }

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
