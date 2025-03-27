package com.belaku.naveenprakash.npstreetmap;

import static com.belaku.naveenprakash.npstreetmap.MainActivity.makeToast;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

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

                MainActivity.markerOptionsGeoFence.title(d_editText_content.getText().toString());
                MainActivity.markerGeoFence.setTitle(d_editText_content.getText().toString().length() > 7 ? d_editText_content.getText().toString().substring(0, 6) + "..," : d_editText_content.getText().toString().substring(0, d_editText_content.getText().length()) + "..,");
                MainActivity.markerGeoFence.showInfoWindow();

                Reminder reminder = new Reminder();
                reminder.setStringTask("R. " + d_editText_content.getText().toString());
                reminder.setLatLng(ll);
                MainActivity.reminders.add(reminder);
                MainActivity.rdbManager.insert(reminder.getStringTask().toString(), String.valueOf(MainActivity.markerGeoFence.getPosition().latitude), String.valueOf(MainActivity.markerGeoFence.getPosition().longitude));
                dismiss();
                makeToast("GeoFence Reminder - " + d_editText_content.getText() + " added!");
            }
        });


    }

}
