package com.belaku.naveenprakash.npstreetmap

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


class MyReceiver : BroadcastReceiver() {
    var nRem: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        val notificationHelper: NotificationHelper = NotificationHelper(context)
        val geofencingEvent = checkNotNull(GeofencingEvent.fromIntent(intent!!))
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...")
            MainActivity.makeToast("onReceive: Error receiving geofence event...");
            return
        }
        val geofenceList = geofencingEvent.triggeringGeofences
        for (geofence in geofenceList!!) {
            Log.d(TAG, "onReceive: " + geofence.requestId)
         //   MainActivity.makeToast("onReceive: " + geofence.requestId);
        }
        val transitionType = geofencingEvent.geofenceTransition
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER - " + geofencingEvent.triggeringGeofences!!.get(0).latitude, Toast.LENGTH_SHORT).show()

                for (i in MainActivity.reminders.indices) {
                    if (geofencingEvent.triggeringGeofences!!.get(0).latitude == MainActivity.reminders.get(i).latLng.latitude)
                        nRem = MainActivity.reminders.get(i).stringTask;
                }

                notificationHelper.sendHighPriorityNotification(
                    "GEOFENCE_TRANSITION_ENTER - " + "Reminding you to ~ ", nRem,
                    MainActivity::class.java
                )
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification(
                    "GEOFENCE_TRANSITION_DWELL", "",
                    MainActivity::class.java
                )
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification(
                    "GEOFENCE_TRANSITION_EXIT" + "Reminding you to ~ " +  nRem, "",
                    MainActivity::class.java
                )
            }
        }
    }
}