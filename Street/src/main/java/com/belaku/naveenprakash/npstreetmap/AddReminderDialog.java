package com.belaku.naveenprakash.npstreetmap;

import static com.belaku.naveenprakash.npstreetmap.MainActivity.MyGmap;
import static com.belaku.naveenprakash.npstreetmap.MainActivity.appContext;
import static com.belaku.naveenprakash.npstreetmap.MainActivity.makeToast;
import static com.belaku.naveenprakash.npstreetmap.MainActivity.markerOptionsGeoFence;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.ui.IconGenerator;

public class AddReminderDialog extends Dialog {


    LatLng ll;

    public Button d_button_ok, d_button_cancel;
    public EditText d_editText_content;

    public AddReminderDialog(Activity a, LatLng l_latlng) {
        super(a);
        ll = l_latlng;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_reminder);

        d_button_ok = findViewById(R.id.d_btn_ok);
        d_button_cancel = findViewById(R.id.d_btn_cancel);
        d_editText_content = findViewById(R.id.d_edtx_content);

        d_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markerOptionsGeoFence = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromBitmap(new IconGenerator(getContext().getApplicationContext()).makeIcon(d_editText_content.getText())));

                markerOptionsGeoFence.title(d_editText_content.getText().toString());

                MainActivity.markerGeoFence = MyGmap.addMarker(markerOptionsGeoFence);

                MainActivity.markerGeoFence.setTitle(d_editText_content.getText().toString().length() > 7 ? d_editText_content.getText().toString().substring(0, 6) + "..," : d_editText_content.getText().toString().substring(0, d_editText_content.getText().length()) + "..,");
                MainActivity.markerGeoFence.showInfoWindow();

                CircleOptions circleOptionsGoeFence = new CircleOptions();
                circleOptionsGoeFence.center(ll);
                circleOptionsGoeFence.radius(250.0);
                circleOptionsGoeFence.strokeColor(R.color.colorAccent);
                circleOptionsGoeFence.fillColor(R.color.colorPrimary);
                circleOptionsGoeFence.strokeWidth(5);
                MyGmap.addCircle(circleOptionsGoeFence);


                Reminder reminder = new Reminder();
                reminder.setStringTask("R. " + d_editText_content.getText().toString());
                reminder.setLatLng(ll);
                MainActivity.reminders.add(reminder);
                MainActivity.rdbManager.insert(reminder.getStringTask().toString(), String.valueOf(MainActivity.markerGeoFence.getPosition().latitude), String.valueOf(MainActivity.markerGeoFence.getPosition().longitude));
                dismiss();
             //   makeToast("GeoFence Reminder - " + d_editText_content.getText() + " added!");

                addGeofence(ll, 1000);

            }
        });


    }

    private void addGeofence(LatLng latLng, float radius) {

        GeoFenceHelper geoFenceHelper = new GeoFenceHelper(appContext);
        String GEOFENCE_ID = "SOME_GEOFENCE_ID";
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(appContext);

        Geofence geofence = geoFenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geoFenceHelper.getGeofencingRequest(geofence);
        Intent intent = new Intent(appContext, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, 2607, intent, PendingIntent.FLAG_MUTABLE);

        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        assert pendingIntent != null;
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "onSuccess: Geofence Added...");
                        makeToast("onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = e.toString();
                        Log.d("TAG", "onFailure: " + errorMessage);
                        makeToast("onFailure: " + errorMessage);
                    }
                });
    }

}
