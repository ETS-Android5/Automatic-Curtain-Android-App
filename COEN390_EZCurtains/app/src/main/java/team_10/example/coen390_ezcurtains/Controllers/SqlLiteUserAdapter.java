package team_10.example.coen390_ezcurtains.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import team_10.example.coen390_ezcurtains.Models.User;

public class SqlLiteUserAdapter extends SQLiteOpenHelper {
    final String Sql_create = "create table users " +
            "(user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username text NOT NULL, " +
            "password text NOT NULL)";

    public SqlLiteUserAdapter(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(Sql_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        onCreate(sqLiteDatabase);
    }

    public boolean Insert(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("username", user.getUserName());
            contentValues.put("password", user.getPassword());
            db.insert("users", null, contentValues);
        }
        catch (Exception exception)
        {
            Log.i("Error(User_DB_insert)", exception.getMessage());
            return false;
        }
        return true;
    }

    public boolean Update(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("password", user.getPassword());
            db.update("users", contentValues, "username = ?", new String[]{ user.getUserName()});
        }
        catch (Exception exception)
        {
            Log.i("Error(User_DB_update)", exception.getMessage());
            return false;
        }
        return true;
    }

    public int Delete(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            return db.delete("users",
                    "username = ? ",
                    new String[] { user.getUserName() });
        }
        catch (Exception exception)
        {
            Log.i("Error(User_DB_delete)", exception.getMessage());
            return -1;
        }
    }

    public List<User> Select(String sql)
    {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst())
        {
            do {
                User user = new User();
                user.setUserName(c.getString(0));
                user.setPassword(c.getString(1));
                users.add(user);
            }while (c.moveToNext());
        }
        return users;
    }
}
