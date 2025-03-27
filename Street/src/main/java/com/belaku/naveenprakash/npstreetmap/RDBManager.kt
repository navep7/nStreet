package com.belaku.naveenprakash.npstreetmap

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase


class RDBManager(private val context: Context) {
    private var dbHelper: RDBHelper? = null

    private var database: SQLiteDatabase? = null

    @Throws(SQLException::class)
    fun open(): RDBManager {
        dbHelper = RDBHelper(context)
        database = dbHelper!!.getWritableDatabase()
        return this
    }

    fun close() {
        dbHelper?.close()
    }

    fun insert(name: String?, lat: String?, lng: String?) {
        val contentValue = ContentValues()
        contentValue.put(RDBHelper.REMINDERNOTE, name)
        contentValue.put(RDBHelper.RLAT, lat)
        contentValue.put(RDBHelper.RLNG, lng)
        database!!.insert(RDBHelper.TABLE_NAME, null, contentValue)
    }

    fun fetch(): Cursor? {
        val columns =
            arrayOf<String>(RDBHelper._ID, RDBHelper.REMINDERNOTE, RDBHelper.RLAT, RDBHelper.RLNG)
        val cursor =
            database!!.query(RDBHelper.TABLE_NAME, columns, null, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
        }
        return cursor
    }

    fun update(_id: Long, name: String?, lat: String?, lng: String?): Int {
        val contentValues = ContentValues()
        contentValues.put(RDBHelper.REMINDERNOTE, name)
        contentValues.put(RDBHelper.RLAT, lat)
        contentValues.put(RDBHelper.RLNG, lng)
        val i = database!!.update(
            RDBHelper.TABLE_NAME,
            contentValues,
            RDBHelper._ID + " = " + _id,
            null
        )
        return i
    }

    fun delete() {
        database!!.delete(RDBHelper.TABLE_NAME, null, null)
    }
}