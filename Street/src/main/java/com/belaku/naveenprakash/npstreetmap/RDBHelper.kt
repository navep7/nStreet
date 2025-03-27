package com.belaku.naveenprakash.npstreetmap

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.android.gms.maps.model.LatLng


class RDBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    companion object {
        // Table Name
        const val TABLE_NAME: String = "DBREMINDERS"

        // Table columns
        const val _ID: String = "_id"
        const val REMINDERNOTE: String = "subject"
        const val RLAT: String = "lat"
        const val RLNG: String = "lng"

        // Database Information
        const val DB_NAME: String = "REMINDERS.DB"

        // database version
        const val DB_VERSION: Int = 1

        // Creating table query
        private const val CREATE_TABLE = ("create table " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REMINDERNOTE + " TEXT NOT NULL, " + RLAT + " TEXT NOT NULL, " + RLNG + " TEXT NOT NULL); ")
    }
}


