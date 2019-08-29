package com.demo.picoandplate.database.model;

/**
 * Created by Alpesh Sorathiya.
 */

public class Log {
    public static final String TABLE_NAME = "logs";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOG = "log";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_PLATE_NO = "plate_number";
    public static final String COLUMN_FINE = "fine";

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    private int id;
    private String log;
    private String timestamp;
    private String plateNumber;
    private String fine = "0";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_LOG + " TEXT,"
                    + COLUMN_PLATE_NO + " TEXT,"
                    + COLUMN_FINE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Log() {
    }

    public Log(int id, String log, String timestamp, String plateNumber, String fine) {
        this.id = id;
        this.log = log;
        this.timestamp = timestamp;
        this.plateNumber = plateNumber;
        this.fine = fine;
    }

    public int getId() {
        return id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String Log) {
        this.log = Log;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
