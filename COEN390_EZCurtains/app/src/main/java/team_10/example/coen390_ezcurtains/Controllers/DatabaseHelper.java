package team_10.example.coen390_ezcurtains.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;
import team_10.example.coen390_ezcurtains.DBConfig;
import team_10.example.coen390_ezcurtains.Models.Alarm;
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
                +DBConfig.COLUMN_ID+" INTEGER PRIMARY KEY NOT NULL, "
                +DBConfig.COLUMN_DEVICES_NAME+" TEXT NOT NULL, "
                +DBConfig.COLUMN_DEVICES_ROOM+" TEXT NOT NULL "+")");

        // Create room table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_ROOMS+"( "
                +DBConfig.COLUMN_ROOMS_NAME+" TEXT PRIMARY KEY NOT NULL "+")");

        // Create schedule table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_SCHEDULES+"( "
                +DBConfig.COLUMN_ID+" INTEGER PRIMARY KEY NOT NULL, "
                +DBConfig.COLUMN_DEVICE_ID+" INTEGER NOT NULL, "
                +DBConfig.COLUMN_SCHEDULES_OPEN+" INTEGER NOT NULL, "
                +DBConfig.COLUMN_SCHEDULES_CLOSE+" INTERGER NOT NULL, "
                +DBConfig.COLUMN_SCHEDULES_MONDAY+" TEXT DEFAULT NULL, "
                +DBConfig.COLUMN_SCHEDULES_TUESDAY+" TEXT DEFAULT NULL, "
                +DBConfig.COLUMN_SCHEDULES_WEDNESDAY+" TEXT DEFAULT NULL, "
                +DBConfig.COLUMN_SCHEDULES_THURSDAY+" TEXT DEFAULT NULL, "
                +DBConfig.COLUMN_SCHEDULES_FRIDAY+" TEXT DEFAULT NULL, "
                +DBConfig.COLUMN_SCHEDULES_SATURDAY+" TEXT DEFAULT NULL, "
                +DBConfig.COLUMN_SCHEDULES_SUNDAY+" TEXT DEFAULT NULL "+")");

        // Create alarm table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_ALARMS+"( "
                +DBConfig.COLUMN_ALARM_ID+" INTEGER PRIMARY KEY NOT NULL, "
                +DBConfig.COLUMN_ALARM_SCHEDULE_ID+" INTEGER NOT NULL, "
                +DBConfig.COLUMN_DAY_OF_WEEK+" INTEGER NOT NULL "+")");

        // Create selected parent items table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_SELECTED_PARENTS+"( "
                +DBConfig.COLUMN_PARENT_ID+" INTEGER PRIMARY KEY NOT NULL "+")");

        // Create selected children items table
        dB.execSQL("CREATE TABLE "+DBConfig.TABLE_SELECTED_CHILDREN+"( "
                +DBConfig.COLUMN_PARENT_ID+" INTEGER NOT NULL, "
                +DBConfig.COLUMN_CHILD_ID+" INTEGER NOT NULL ,"
                +"PRIMARY KEY("+DBConfig.COLUMN_PARENT_ID+", "+DBConfig.COLUMN_CHILD_ID+")"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase dB, int i, int i1) {
        // Drop older tables
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_USERS);
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_DEVICES);
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_ROOMS);
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_SCHEDULES);
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_SELECTED_PARENTS);
        dB.execSQL("DROP TABLE IF EXISTS "+DBConfig.TABLE_SELECTED_CHILDREN);
        // Create new tables
        onCreate(dB);
    }

    public long insertUser(User user) {
        long id = -1;
        SQLiteDatabase dB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_USERS_USERNAME, user.getUserName());
        contentValues.put(DBConfig.COLUMN_USERS_PASSWORD, user.getPassword());
        // Insert row into user table
        id = dB.insert(DBConfig.TABLE_USERS, null, contentValues);
        //dB.close();
        return id;
    }

    public long insertAlarm(Alarm alarm) {
        long id = -1;
        SQLiteDatabase dB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_ALARM_ID, alarm.getAlarmID());
        contentValues.put(DBConfig.COLUMN_ALARM_SCHEDULE_ID, alarm.getScheduleID());
        contentValues.put(DBConfig.COLUMN_DAY_OF_WEEK, alarm.getDay());
        // Insert row into user table
        id = dB.insert(DBConfig.TABLE_ALARMS, null, contentValues);
        dB.close();
        return id;
    }

    public long insertDevice(Device device) {
        long id = -1;
        SQLiteDatabase dB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_DEVICES_NAME, device.getDeviceName());
        contentValues.put(DBConfig.COLUMN_DEVICES_ROOM, device.getRoomName());
        // Insert row into user table
        id = dB.insert(DBConfig.TABLE_DEVICES, null, contentValues);
        //dB.close();
        return id;
    }

    public long insertRoom(Room room) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_ROOMS_NAME, room.getRoomName());
        // Insert row into room table
        id = db.insert(DBConfig.TABLE_ROOMS, null, contentValues);
        //db.close();
        return id;
    }

    public long insertSchedule(Schedule schedule) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_DEVICE_ID, schedule.getDeviceID());
        contentValues.put(DBConfig.COLUMN_SCHEDULES_OPEN, schedule.getOpenTime());
        contentValues.put(DBConfig.COLUMN_SCHEDULES_CLOSE, schedule.getCloseTime());
        contentValues.put(DBConfig.COLUMN_SCHEDULES_MONDAY, schedule.getDaysOfTheWeek().get(0));
        contentValues.put(DBConfig.COLUMN_SCHEDULES_TUESDAY, schedule.getDaysOfTheWeek().get(1));
        contentValues.put(DBConfig.COLUMN_SCHEDULES_WEDNESDAY, schedule.getDaysOfTheWeek().get(2));
        contentValues.put(DBConfig.COLUMN_SCHEDULES_THURSDAY, schedule.getDaysOfTheWeek().get(3));
        contentValues.put(DBConfig.COLUMN_SCHEDULES_FRIDAY, schedule.getDaysOfTheWeek().get(4));
        contentValues.put(DBConfig.COLUMN_SCHEDULES_SATURDAY, schedule.getDaysOfTheWeek().get(5));
        contentValues.put(DBConfig.COLUMN_SCHEDULES_SUNDAY, schedule.getDaysOfTheWeek().get(6));
        // Insert row into schedule table
        id = db.insert(DBConfig.TABLE_SCHEDULES, null , contentValues);
        //db.close();
        return id;
    }

    public long insertSelectedParent(int headerPosition) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_PARENT_ID, headerPosition);
        // Insert row into schedule table
        id = db.insert(DBConfig.TABLE_SELECTED_PARENTS, null, contentValues);
        db.close();
        return id;
    }

    public long insertSelectedChild(int headerPosition, int childPosition) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.COLUMN_PARENT_ID, headerPosition);
        contentValues.put(DBConfig.COLUMN_CHILD_ID, childPosition);
        // Insert row into schedule table
        id = db.insert(DBConfig.TABLE_SELECTED_CHILDREN, null, contentValues);
        db.close();
        return id;
    }

    public long removeSchedule(int scheduleID) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        id = db.delete(DBConfig.TABLE_SCHEDULES, DBConfig.COLUMN_ID+" = ?", new String[]{Integer.toString(scheduleID)});
        db.close();
        return id;
    }

    public void removeSelectedParent(int headerPosition) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBConfig.TABLE_SELECTED_PARENTS, DBConfig.COLUMN_PARENT_ID+" = ?", new String[]{Integer.toString(headerPosition)});
        db.close();
    }

    public void removeSelectedChild(int headerPosition, int childPosition) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBConfig.TABLE_SELECTED_CHILDREN, DBConfig.COLUMN_PARENT_ID+" = ? AND "+DBConfig.COLUMN_CHILD_ID+"=?", new String[]{Integer.toString(headerPosition), Integer.toString(childPosition)});
        db.close();
    }

    public boolean checkSelectedParent(int headerPosition) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+DBConfig.TABLE_SELECTED_PARENTS
                +" WHERE "+ DBConfig.COLUMN_PARENT_ID+" = "+headerPosition;
        Cursor cursor = db.rawQuery(query, null);
        boolean result = cursor.getCount() > 0;
        db.close();
        return result;
    }

    public boolean checkSelectedChild(int headerPosition, int childPosition) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+DBConfig.TABLE_SELECTED_CHILDREN
                +" WHERE "+ DBConfig.COLUMN_PARENT_ID+" = "+headerPosition+" AND "+DBConfig.COLUMN_CHILD_ID+" = "+childPosition;
        Cursor cursor = db.rawQuery(query, null);
        boolean result = cursor.getCount() > 0;
        db.close();
        return result;
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
        //db.close();
        return result;
    }

    public Alarm getAlarm(int scheduleID, int day) {
        Alarm alarm = new Alarm();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+DBConfig.TABLE_ALARMS
                +" WHERE "+DBConfig.COLUMN_DAY_OF_WEEK+" = "+day+" AND "+DBConfig.COLUMN_ALARM_SCHEDULE_ID+" = "+scheduleID;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        alarm.setAlarmID(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_ALARM_ID))));
        alarm.setDay(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_DAY_OF_WEEK))));
        alarm.setScheduleID(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_ALARM_SCHEDULE_ID))));
        return alarm;
    }

    // Get selected parents
    public List<Integer> getSelectedParents() {
        List<Integer> selectedParents = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+DBConfig.TABLE_SELECTED_PARENTS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                selectedParents.add(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_PARENT_ID))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return selectedParents;
    }

    // Get selected children
    public HashMap<Integer, List<Integer>> getSelectedChildren() {
        HashMap<Integer, List<Integer>> selectedChildren = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+DBConfig.TABLE_SELECTED_CHILDREN;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int headerPos = cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_PARENT_ID)));
                int childPos = cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_CHILD_ID)));
                // Check if key is present, if not add key with new list
                if (!(selectedChildren.containsKey(headerPos))) {
                    selectedChildren.put(headerPos, new ArrayList<Integer>());
                }
                selectedChildren.get(headerPos).add(childPos);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return selectedChildren;
    }

    // Get schedule for given device
    public List<Schedule> getSchedule(int id) {
        List<Schedule> scheduleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+DBConfig.TABLE_SCHEDULES+" WHERE "+DBConfig.COLUMN_DEVICE_ID+" = "+id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                List<Integer> daysList = new ArrayList<>();
                Schedule schedule = new Schedule();
                schedule.setScheduleID(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_ID))));
                schedule.setDeviceID(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_DEVICE_ID))));
                schedule.setOpenTime(cursor.getLong((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_OPEN))));
                schedule.setCloseTime(cursor.getLong((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_CLOSE))));
                daysList.add(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_MONDAY))));
                daysList.add(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_TUESDAY))));
                daysList.add(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_WEDNESDAY))));
                daysList.add(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_THURSDAY))));
                daysList.add(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_FRIDAY))));
                daysList.add(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_SATURDAY))));
                daysList.add(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_SCHEDULES_SUNDAY))));
                schedule.setDaysOfTheWeek(daysList);
                scheduleList.add(schedule);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scheduleList;
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
                device.setDeviceID(cursor.getInt((cursor.getColumnIndex(DBConfig.COLUMN_ID))));
                device.setDeviceName(cursor.getString((cursor.getColumnIndex(DBConfig.COLUMN_DEVICES_NAME))));
                device.setRoomName(cursor.getString((cursor.getColumnIndex(DBConfig.COLUMN_DEVICES_ROOM))));

                // Check if key is present, if not add key with new list
                if (!(childrenList.containsKey(device.getRoomName()))) {
                    childrenList.put(device.getRoomName(), new ArrayList<Device>());
                }
                childrenList.get(device.getRoomName()).add(device);

            }while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
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
        cursor.close();
        //db.close();
        return roomList;
    }

}