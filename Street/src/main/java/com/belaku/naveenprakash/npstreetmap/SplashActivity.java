package com.belaku.naveenprakash.npstreetmap;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class SplashActivity extends AppCompatActivity {

    private static final int MY_L_PERMISSION_REQUEST = 1, LOC_BG_P = 2, MY_N_PERMISSION_REQUEST = 3;
    private static final int REQ_LOC = 2;
    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = -10.0f * 360.0f;

    private TextView Tx;
    private ImageView Imgv;
    private RotateAnimation mRotateAnimation;
    Handler handler = new Handler();
//    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Tx = (TextView) findViewById(R.id.s_tx);
        Imgv = (ImageView) findViewById(R.id.s_imgv);

        mRotateAnimation = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//0, 0, 40, 0);
        mRotateAnimation.setDuration((long) 2 * 5000);
        mRotateAnimation.setRepeatCount(0);

        Imgv.setAnimation(mRotateAnimation);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        prominentDisclosure();
        else GoToMain();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void prominentDisclosure() {
        AlertDialog alertDialog;
        {
            alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
            alertDialog.setTitle("Welcome to Street Maps");
            alertDialog.setMessage("This App collects location data enabling Users to get their present location address in String and will let them share to any of their contacts using messaging means.. \n The App also accesses Location in the background to remind Users to get something done, when they visit a location they desire.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Agree",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                checkLocationPermission();

                            } else
                                makeToast("requesting Location Permissions now \n Grant permissions for app to work ");
                            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_L_PERMISSION_REQUEST);
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_L_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //    checkGPS();
                makeToast("Location permission granted now");
                requestBgPermission();
            }
        } else if (requestCode == LOC_BG_P) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkNotificationPermission();
            }
        }  else if (requestCode == MY_N_PERMISSION_REQUEST)
            GoToMain();
            else makeToast("App can't work without access to GPS");
    }

    private void requestBgPermission() {
        if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            makeToast("Allow all the time - for Location Reminders to work correctly");
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, LOC_BG_P);
        }
    }


    private boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            ConnectivityManager connectivitymanager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivitymanager.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            if (!isConnected) {
                Tx.setText("Check your internet connectivity and try again, later : \n              NETWORK ERROR");
            } else checkGPS();

            return true;

        } else
            makeToast("requesting Location Permissions now \n Grant permissions for app to work ");
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, MY_L_PERMISSION_REQUEST);
        return false;
    }

    @SuppressLint("InlinedApi")
    private void checkNotificationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, MY_N_PERMISSION_REQUEST);
        }
    }

    private void checkGPS() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            BuildGPSalert();
        } else GoToMain();
    }

    private void BuildGPSalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        //         spinner.setVisibility(View.INVISIBLE);
                        makeToast("App can't work without access to GPS .. \n \t Enable now ?");


                        BuildGPSalert();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void GoToMain() {
        Tx.setText("- Naveen Prakash");

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
                {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }

            }


        }, 1000);
    }
}
