package com.belaku.naveenprakash.npstreetmap;


import com.google.android.gms.maps.model.LatLng;

public class Reminder {
    private String stringTask;
    private LatLng latLng;
    public static final String R_COLUMN_ID = "id";
    public static final String COLUMN_R = "reminder";
    public static final String TABLE_NAME = "reminders";

    public static final String CREATE_TABLE_REMINDER =
            "CREATE TABLE " + TABLE_NAME + "("
                    + R_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_R + " TEXT"
                    + ")";

    public Reminder(String stringTask, LatLng latLng) {
        this.stringTask = stringTask;
        this.latLng = latLng;

    }

    public Reminder() {

    }


    public String getStringTask() {
        return stringTask;
    }

    public void setStringTask(String stringTask) {
        this.stringTask = stringTask;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
