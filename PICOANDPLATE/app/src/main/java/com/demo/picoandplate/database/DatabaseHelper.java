package com.demo.picoandplate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.demo.picoandplate.database.model.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alpesh Sorathiya.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "logs_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create logs table
        db.execSQL(Log.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Log.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertLog(String log, String plateNumber, String fine) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Log.COLUMN_LOG, log);
        values.put(Log.COLUMN_PLATE_NO, plateNumber);
        values.put(Log.COLUMN_FINE, fine);

        // insert row
        long id = db.insert(Log.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Log getLog(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Log.TABLE_NAME,
                new String[]{Log.COLUMN_ID, Log.COLUMN_LOG, Log.COLUMN_PLATE_NO, Log.COLUMN_FINE, Log.COLUMN_TIMESTAMP},
                Log.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare log object
        Log log = new Log(
                cursor.getInt(cursor.getColumnIndex(Log.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Log.COLUMN_LOG)),
                cursor.getString(cursor.getColumnIndex(Log.COLUMN_PLATE_NO)),
                cursor.getString(cursor.getColumnIndex(Log.COLUMN_FINE)),
                cursor.getString(cursor.getColumnIndex(Log.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return log;
    }

    public List<Log> getAllLogs() {
        List<Log> logs = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Log.TABLE_NAME + " ORDER BY " +
                Log.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log log = new Log();
                log.setId(cursor.getInt(cursor.getColumnIndex(Log.COLUMN_ID)));
                log.setLog(cursor.getString(cursor.getColumnIndex(Log.COLUMN_LOG)));
                log.setPlateNumber(cursor.getString(cursor.getColumnIndex(Log.COLUMN_PLATE_NO)));
                log.setFine(cursor.getString(cursor.getColumnIndex(Log.COLUMN_FINE)));
                log.setTimestamp(cursor.getString(cursor.getColumnIndex(Log.COLUMN_TIMESTAMP)));
                logs.add(log);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return logs list
        return logs;
    }

    public List<Log> getAllLogs(String plateNumber) {
        List<Log> logs = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Log.TABLE_NAME + " WHERE " + Log.COLUMN_PLATE_NO + "='" + plateNumber + "' ORDER BY " +
                Log.COLUMN_TIMESTAMP + " DESC";

        android.util.Log.i("Hello", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log log = new Log();
                log.setId(cursor.getInt(cursor.getColumnIndex(Log.COLUMN_ID)));
                log.setLog(cursor.getString(cursor.getColumnIndex(Log.COLUMN_LOG)));
                log.setPlateNumber(cursor.getString(cursor.getColumnIndex(Log.COLUMN_PLATE_NO)));
                log.setFine(cursor.getString(cursor.getColumnIndex(Log.COLUMN_FINE)));
                log.setTimestamp(cursor.getString(cursor.getColumnIndex(Log.COLUMN_TIMESTAMP)));
                logs.add(log);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return logs list
        return logs;
    }

    public int getLogsCount() {
        String countQuery = "SELECT  * FROM " + Log.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateLog(Log log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Log.COLUMN_LOG, log.getLog());

        // updating row
        return db.update(Log.TABLE_NAME, values, Log.COLUMN_ID + " = ?",
                new String[]{String.valueOf(log.getId())});
    }

    public void deleteLog(Log log) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Log.TABLE_NAME, Log.COLUMN_ID + " = ?",
                new String[]{String.valueOf(log.getId())});
        db.close();
    }
}
