package team_10.example.coen390_ezcurtains.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import team_10.example.coen390_ezcurtains.DBConfig;
import team_10.example.coen390_ezcurtains.Models.Device;
import team_10.example.coen390_ezcurtains.Models.Room;
import team_10.example.coen390_ezcurtains.Models.Schedule;
import team_10.example.coen390_ezcurtains.Models.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DBConfig.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dB) {
        // Create user table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_USERS+"( "
                +DBConfig.COLUMN_USERS_USERNAME+" TEXT PRIMARY KEY NOT NULL,"
                +DBConfig.COLUMN_USERS_PASSWORD+" TEXT NOT NULL "+")");

        // Create device table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_DEVICES+"( "
                +DBConfig.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                +DBConfig.COLUMN_DEVICES_NAME+" TEXT NOT NULL, "
                +DBConfig.COLUMN_DEVICES_ROOM+" TEXT NOT NULL, "
                +DBConfig.COLUMN_DEVICES_SCHEDULE+" INTEGER "+")");

        // Create room table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_ROOMS+"( "
                +DBConfig.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                +DBConfig.COLUMN_ROOMS_NAME+" TEXT NOT NULL "+")");

        // Create schedule table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_SCHEDULES+"( "
                +DBConfig.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                +DBConfig.COLUMN_SCHEDULES_OPEN+" TEXT NOT NULL, "
                +DBConfig.COLUMN_SCHEDULES_CLOSE+" TEXT NOT NULL "+")");

//        User user = new User();
//        user.setUserName("Zeineb");
//        user.setPassword("Team10!");
//        insertUser(user);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dB, int i, int i1) {
        // Drop older tables
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_USERS);
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_DEVICES);
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_ROOMS);
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_SCHEDULES);
        // Create new tables
        onCreate(dB);
    }

    public long insertUser(User user) {
        long id = -1;
        SQLiteDatabase dB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_USERS_USERNAME, user.getUserName());
        contentValues.put(DBConfig.COLUMN_USERS_PASSWORD, user.getPassword());
        // Insert row int user table
        id = dB.insert(DBConfig.TABLE_USERS, null, contentValues);
        dB.close();
        return id;
    }

    public long insertDevice(Device device) {
        long id = -1;
        SQLiteDatabase dB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_DEVICES_NAME, device.getDeviceName());
        contentValues.put(DBConfig.COLUMN_DEVICES_ROOM, device.getRoomName());
        contentValues.put(DBConfig.COLUMN_DEVICES_SCHEDULE, device.getScheduleID());
        // Insert row into user table
        id = dB.insert(DBConfig.TABLE_DEVICES, null, contentValues);
        dB.close();
        return id;
    }

    public long insertRoom(Room room) {
        long id = -1;
        SQLiteDatabase dB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_ROOMS_NAME, room.getRoomName());
        // Insert row into room table
        id = dB.insert(DBConfig.TABLE_ROOMS, null, contentValues);
        dB.close();
        return id;
    }

    // Check if room name already exists
    public boolean checkRoom(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+DBConfig.COLUMN_ROOMS_NAME
                +" FROM "+DBConfig.TABLE_ROOMS
                +" WHERE "+ DBConfig.COLUMN_ROOMS_NAME+" = "+roomName;
        Cursor cursor = db.rawQuery(query, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    // Get schedule for given device
    public Schedule getSchedule(int id) {
        Schedule schedule = new Schedule();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+ DBConfig.COLUMN_SCHEDULES_OPEN
                +", "+DBConfig.COLUMN_SCHEDULES_CLOSE
                +" FROM "+DBConfig.TABLE_SCHEDULES
                +", "+DBConfig.TABLE_DEVICES
                +" WHERE "+ DBConfig.TABLE_DEVICES+"."+DBConfig.COLUMN_DEVICES_SCHEDULE+" = "+DBConfig.TABLE_SCHEDULES+"."+DBConfig.COLUMN_ID
                +" AND "+DBConfig.TABLE_DEVICES+"."+DBConfig.COLUMN_ID+" = "+id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        schedule.setScheduleID(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_ID))));
        schedule.setOpenTime(cursor.getString((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_OPEN))));
        schedule.setCloseTime(cursor.getString((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_CLOSE))));

        return schedule;
    }

    // Get hashmap with room-device pairs
    public HashMap<String, List<Device>> getDevices() {
        HashMap<String, List<Device>> childrenList = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+DBConfig.TABLE_DEVICES;
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Device device = new Device();
                device.setDeviceName(cursor.getString((cursor.getColumnIndex(DBConfig.COLUMN_DEVICES_NAME))));
                device.setRoomName(cursor.getString((cursor.getColumnIndex(DBConfig.COLUMN_DEVICES_ROOM))));
                device.setScheduleID(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_DEVICES_SCHEDULE))));

                // Check if key is present, if not add key with new list
                if (!(childrenList.containsKey(device.getRoomName()))) {
                    childrenList.put(device.getRoomName(), new ArrayList<Device>());
                }
                childrenList.get(device.getRoomName()).add(device);

            }while (cursor.moveToNext());
        }
        return childrenList;
    }

    // Get rooms
    public List<Room> getRooms() {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+DBConfig.TABLE_ROOMS;
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                room.setRoomName(cursor.getString((cursor.getColumnIndex(DBConfig.COLUMN_ROOMS_NAME))));
                roomList.add(room);
            }while (cursor.moveToNext());
        }
        return roomList;
    }

    // Get user
}