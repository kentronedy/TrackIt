package com.aloydev.weighttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;

public class WeightDatabase extends SQLiteOpenHelper {
    public WeightDatabase(Context context) {
        super(context, "UserWeights.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create the tables
        db.execSQL("create Table Users(" +
                "username TEXT primary key, " +
                "password TEXT not NULL, " +
                "goal REAL, " +
                "permission INTEGER default 0 not NULL)");
        db.execSQL("create Table Weights(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "date TEXT not NULL, " +
                "weight REAL not NULL, " +
                "dateInt INTEGER not NULL, "+
                "username TEXT not NULL, " +
                "foreign key (username) " +
                    "references Users (username)" +
                        "on delete cascade " +
                        "on update no action)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Users");
        db.execSQL("drop Table if exists Weights");
    }

    //create method for the users table
    public boolean createUserData(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("Users", null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    //read method for the users table, returns a cursor
    public Cursor readUserData(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Users where username = ?", new String[]{username});
        return cursor;
    }

    //update method for the users table
    public boolean updateUserData(String username, float goal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goal", goal);
        Cursor cursor = db.rawQuery("Select * from Users where username = ?", new String[]{username});
        if(cursor.getCount() > 0){
            long result = db.update("Users", values, "username = ?", new String[]{username});
            if(result == -1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    //overloaded method for updating the users table
    public boolean updateUserData(String username, int permission){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("permission", permission);
        Cursor cursor = db.rawQuery("Select * from Users where username = ?", new String[]{username});
        if(cursor.getCount() > 0){
            long result = db.update("Users", values, "username = ?", new String[]{username});
            if(result == -1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    //delete method for the users table
    public boolean deleteUserData(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Users where username = ?", new String[]{username});
        if(cursor.getCount() > 0){
            long result = db.delete("Users","username = ?", new String[]{username});
            if(result == -1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //create method for the weights table
    public boolean createWeightData(Date date, float weight, String username){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);
        long dateInt = date.getTime();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", stringDate);
        values.put("weight", weight);
        values.put("username", username);
        values.put("dateInt", dateInt);
        long result = db.insert("Weights", null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    //read method for the weights table
    public Cursor readWeightData(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery("Select * from Weights where username = ? order by dateInt DESC", new String[]{username});
        return cursor;
    }

    //overloaded method to read data in the weights table
    public Cursor readWeightData(String stringDate, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery("Select * from Weights where date = ? and username = ?", new String[]{stringDate, username});
        return cursor;
    }

    //overloaded method to read data from the weights table
    public Cursor readWeightData(Date date, String username){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery("Select * from Weights where date = ? and username = ?", new String[]{stringDate, username});
        return cursor;
    }

    //Update method for the weights table
    public boolean updateWeightData(String stringDate, float weight, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("weight", weight);
        Cursor cursor = db.rawQuery("Select * from Weights where date = ? and username = ?", new String[]{stringDate, username});
        if(cursor.getCount() > 0){
            long result = db.update("Weights", values, "date = ? and username = ?", new String[]{stringDate, username});
            if(result == -1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //delete method for the weights table
    public boolean deleteWeightData(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery("Select * from Weights where date = ?", new String[]{date});
        if(cursor.getCount() > 0){
            long result = db.delete("Weights", "date = ?", new String[]{date});
            if(result == -1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
