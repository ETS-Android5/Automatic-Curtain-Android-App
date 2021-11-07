package team_10.example.coen390_ezcurtains.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
                +DBConfig.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                +DBConfig.COLUMN_USERS_USERNAME+" TEXT NOT NULL,"
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
        // Insert row int user table
        id = dB.insert(DBConfig.TABLE_USERS, null, contentValues);
        dB.close();
        return id;
    }

    public long insertRoom(Room room) {
        long id = -1;
        SQLiteDatabase dB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_ROOMS_NAME, room.getRoomName());
        // Insert row int user table
        id = dB.insert(DBConfig.TABLE_USERS, null, contentValues);
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


}