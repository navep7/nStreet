package com.belaku.naveenprakash.npstreetmap;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//STYLING KEY - AIzaSyCQ_rTIUq6Tt53kfCK0etW_7HgXZQeYO8s

//Google Places API key - AIzaSyCg2S0L5a79T8XgOozcP76lFdnYmyblWgU

public class MainActivity extends AppCompatActivity implements OnUserEarnedRewardListener, OnMapReadyCallback, View.OnTouchListener, RvAdapter.OnPlaceClick {

    private boolean prodFlag = false;

    private static final String TAG = "MainActivity";
    private static final int LOB_BG_P = 1;
    public static List<Reminder> reminders = new ArrayList<>();


    // Naveen Prakash

    static Context appContext;
    private SupportMapFragment mSupportMapFragment;
    public static Location MyLocation;
    private static MarkerOptions markerOptions;
    private SupportStreetViewPanoramaFragment mSupportStreetViewPanoramaFragment;
    public static StreetViewPanorama mStreetViewPanorama;
    private FloatingActionButton fabTurnByTurnDirections, fabSetWall;

    private RadioGroup rgViews;
    public static GoogleMap MyGmap;
    private static List<Address> addresses;
    public static LatLng mLatLng;
    private static String mAddress;
    private String mTime, mDate;


    private int _xDelta, _yDelta;


    private RotateAnimation mRotateAnimation;
    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = -10.0f * 360.0f;
    private FrameLayout.LayoutParams fabRotatelayoutParams;
    private StreetViewPanoramaCamera mStreetViewPanoramaCamera;

    Handler streetHandler = new Handler();
    int streetDelay = 2000;


    private TextView TxPlaceDetails, TxStreetView, btnRemoveFences;

    private CoordinatorLayout mainLayout;

    private double addressLatitude, addressLongitude;
    private boolean firstView = true;
    //   private CarmenFeature featureTransit;
    private LatLng destLocation;
    private ArrayList<String> arrayListNearArray = new ArrayList<>();

    private int screenWidth, screenHeight;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private ProgressDialog pd;
    private AdView bannerAdView;

    private List<String> arrayListPlaces = Arrays.asList("Amsterdam, North Holland, Netherlands", "Barcelona, Catalonia, Spain", "Cairo, Egypt", "Dallas, Texas, USA", "Edinburgh, Scotland",
            "Florence, Italy", "Ghent, Gent, Belgium", "Havana, La Habana Vieja, Cuba", "Indianapolis, Indiana, USA", "Jacksonville, Florida, USA",
            "Kansas City, Missouri, USA", "Las Vegas, Nevada, USA", "Mysuru, Karnataka, India", "New Delhi, India", "Otty, Tamil Nadu, India",
            "Paris, France", "Quincy, Massachusetts, USA", "Rome, Roma Capitale, Italy", "Sydney, New South Wales, Australia", "Tokyo, Japan",
            "Uraguay Drive, Pasadena, TX 77504, USA", "Vancouver, BC, Canada", "Washington, District of Columbia, USA", "Xi'an, Shaanxi, China", "Yadiyūr, Tumkūr, India", "Zaragoza, Aragon, Spain");
    private List<LatLng> arrayListPlacesLatLng = new ArrayList<>();
    private ImageButton mapSize;
    private boolean normalView = true;
    public static MarkerOptions markerOptionsGeoFence;
    public static Marker markerGeoFence;
    private GeofencingRequest geofencingRequest;
    private PendingIntent pendingIntentGeoFence;


    public static RDBManager rdbManager;
    public static Geofence geofence;
    private FloatingActionButton fabPlaces;
    private EditText editTextPlaceStreet, editTextPlaceMap;
    private ListView listViewPlaces;
    private AdLoader adLoader1, adLoader2;
    private boolean adLoaded1 = false, adLoaded2 = false;
    TemplateView templateView1;
    private HorizontalScrollView hScrollViewPlaces;
    private ImageButton imgbtn_street_expand_contract;
    private ArrayList<Places> placesNearby;
    private Polyline polyLineRoute;
    private Marker markerAddress;
    private String[] mAddresses;
    private boolean aPlaceSearched = false;
    private int delayCount = 0;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LatLng streetViewPos;
    private boolean streetPicSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Mapbox.getInstance(this,getString(R.string.mapbox_token));
        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();

        findViewByIds();

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;


        RelativeLayout.LayoutParams rlpHS = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlpHS.addRule(RelativeLayout.CENTER_VERTICAL);
        templateView1.setLayoutParams(rlpHS);


        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mSupportStreetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager()
                .findFragmentById(R.id.streetviewpanorama);


        MobileAds.initialize(this);


        nativeAd();

        mainLayout = findViewById(R.id.main_layout);

        btnRemoveFences = findViewById(R.id.btn_remove_fences);
        rdbManager = new RDBManager(this);
        rdbManager.open();
        Cursor cursor = rdbManager.fetch();
    //    makeToast("Reminders - " + cursor.getCount());

        if (cursor.getCount() > 0)
            btnRemoveFences.setVisibility(View.VISIBLE);
        else btnRemoveFences.setVisibility(View.INVISIBLE);

        btnRemoveFences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyGmap.clear();
                rdbManager.delete();
            }
        });
        //ADs
        for (int i = 0; i < cursor.getCount(); i++) {
            if (i == 0)
                cursor.moveToFirst();
            else cursor.moveToNext();
            Reminder reminder = new Reminder();
            reminder.setStringTask(cursor.getString(1));
            reminder.setLatLng(new LatLng(Double.valueOf(cursor.getString(2)), Double.valueOf(cursor.getString(3))));
            reminders.add(reminder);
        }

        //  adView.setLayoutParams(rlp);
        //   findViewById(R.id.main_layout)
        mapSize = findViewById(R.id.map_size);


        fabTurnByTurnDirections = findViewById(R.id.fab_turndirections);

        fabTurnByTurnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + destLocation.latitude + "," + destLocation.longitude + "&mode=1"));
                intent.setPackage("com.google.android.apps.maps");

                startActivity(intent);
            }
        });


        ArrayList<String> cList = new ArrayList<String>();

        String[] locales = Locale.getISOCountries();

        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            System.out.println("Country Name = " + obj.getDisplayCountry());
            cList.add(obj.getDisplayCountry());

        }


        TxPlaceDetails = findViewById(R.id.tx_place_details);
        TxPlaceDetails.bringToFront();
        fabPlaces = findViewById(R.id.fab_places);
        imgbtn_street_expand_contract = findViewById(R.id.imgbtn_street_expand_contract);

        imgbtn_street_expand_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fsStreetPic();
            }
        });
        editTextPlaceStreet = findViewById(R.id.edtx_search_street);
        editTextPlaceStreet.setTypeface(null, Typeface.BOLD);
        editTextPlaceStreet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    aPlaceSearched = true;
                    searchPlace(editTextPlaceStreet.getText().toString());
                    //   editTextPlace.setVisibility(View.INVISIBLE);
                    handled = true;
                }
                return handled;
            }
        });

        editTextPlaceMap = findViewById(R.id.edtx_search_map);
        editTextPlaceMap.setTypeface(null, Typeface.BOLD);
        editTextPlaceMap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getPlaces(editTextPlaceMap.getText().toString());
                        }
                    });
                    thread.start();
                }
                return handled;
            }
        });

        fabPlaces.bringToFront();
        editTextPlaceStreet.bringToFront();
        listViewPlaces = new ListView(MainActivity.this);

        listViewPlaces.setAdapter(new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                arrayListPlaces));
        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeToast(arrayListPlaces.get(position));
                listViewPlaces.setVisibility(View.INVISIBLE);
                mStreetViewPanorama.setPosition(arrayListPlacesLatLng.get(position));
                //    loadFS();
            }
        });


        fabPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextPlaceStreet.setText("");
                listViewPlacesDialog();
                aPlaceSearched = true;
                if (arrayListPlacesLatLng.isEmpty()) {
                    arrayListPlacesLatLng.add(new LatLng(52.3730796, 4.8924534));
                    arrayListPlacesLatLng.add(new LatLng(41.3828939, 2.1774322));
                    arrayListPlacesLatLng.add(new LatLng(30.0443879, 31.2357257));
                    arrayListPlacesLatLng.add(new LatLng(32.7762719, -96.7968559));
                    arrayListPlacesLatLng.add(new LatLng(55.9533456, -3.1883749));

                    arrayListPlacesLatLng.add(new LatLng(43.7697955, 11.2556404));
                    arrayListPlacesLatLng.add(new LatLng(51.0538286, 3.7250121));
                    arrayListPlacesLatLng.add(new LatLng(23.135305, -82.3589631));
                    arrayListPlacesLatLng.add(new LatLng(39.7683331, -86.1583502));
                    arrayListPlacesLatLng.add(new LatLng(30.3321838, -81.655651));

                    arrayListPlacesLatLng.add(new LatLng(39.100105, -94.5781416));
                    arrayListPlacesLatLng.add(new LatLng(36.1672559, -115.148516));
                    arrayListPlacesLatLng.add(new LatLng(12.3051828, 76.6553609));
                    arrayListPlacesLatLng.add(new LatLng(28.6419258, 77.2217499));
                    arrayListPlacesLatLng.add(new LatLng(11.4126769, 76.7030504));

                    arrayListPlacesLatLng.add(new LatLng(48.8534951, 2.3483915)); //P1
                    arrayListPlacesLatLng.add(new LatLng(42.2509914, -71.0037374));
                    arrayListPlacesLatLng.add(new LatLng(41.8933203, 12.4829321));
                    arrayListPlacesLatLng.add(new LatLng(-33.8698439, 151.2082848));
                    arrayListPlacesLatLng.add(new LatLng(35.6821936, 139.762221));

                    arrayListPlacesLatLng.add(new LatLng(29.6547627, -95.1819818));
                    arrayListPlacesLatLng.add(new LatLng(49.2608724, -123.113952));
                    arrayListPlacesLatLng.add(new LatLng(38.8950368, -77.0365427));
                    arrayListPlacesLatLng.add(new LatLng(34.261004, 108.9423363));
                    arrayListPlacesLatLng.add(new LatLng(12.97178, 76.86291));
                    arrayListPlacesLatLng.add(new LatLng(41.6521342, -0.8809428));

                }


                makeToast(mStreetViewPanorama.getLocation() + " !");

            }
        });

        TxPlaceDetails.setBackground(getResources().getDrawable(R.drawable.tx_bg));

        mRotateAnimation = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//0, 0, 40, 0);
        mRotateAnimation.setDuration((long) 5 * 5000);
        mRotateAnimation.setRepeatCount(0);

        fabRotatelayoutParams = new FrameLayout.LayoutParams(200, 200);
        fabRotatelayoutParams.gravity = Gravity.CENTER_VERTICAL;


        getTimeandDate();

        Log.d("Severe", "calling getLastKnownLocation");
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("fetching Location");
        pd.show();
        getLastKnownLocation();

    }

    private void findViewByIds() {
        templateView1 = findViewById(R.id.nativeTemplateView1);
        hScrollViewPlaces = findViewById(R.id.h_scrollview);
    }

    private void fsStreetPic() {

        if (mSupportStreetViewPanoramaFragment.getView().getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
            loadFS();
            editTextPlaceStreet.setVisibility(View.VISIBLE);
            fabPlaces.setVisibility(View.VISIBLE);
            mSupportStreetViewPanoramaFragment.getView().setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mSupportStreetViewPanoramaFragment.getView().bringToFront();
        } else {
            editTextPlaceStreet.setVisibility(View.INVISIBLE);
            fabPlaces.setVisibility(View.INVISIBLE);
            templateView1.bringToFront();
            RelativeLayout.LayoutParams rlpSP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
            rlpSP.addRule(RelativeLayout.ABOVE, templateView1.getId());
            mSupportStreetViewPanoramaFragment.getView().setLayoutParams(rlpSP);
        }
        editTextPlaceStreet.bringToFront();
        TxPlaceDetails.bringToFront();
        imgbtn_street_expand_contract.bringToFront();
    }


    private String getPlaceUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 10000);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private void fsMap() {

        if (mSupportMapFragment.getView().getLayoutParams().height != screenHeight - 150) {
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight - 150);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mSupportMapFragment.getView().setLayoutParams(rlp);
            mSupportMapFragment.getView().bringToFront();

            hScrollViewPlaces.setVisibility(View.VISIBLE);
            rgViews.setVisibility(View.VISIBLE);
            editTextPlaceMap.setVisibility(View.VISIBLE);

            loadFS();
        } else {

            hScrollViewPlaces.setVisibility(View.INVISIBLE);
            rgViews.setVisibility(View.INVISIBLE);
            editTextPlaceMap.setVisibility(View.INVISIBLE);

            MyGmap.clear();
            addPresentMarker();
            templateView1.bringToFront();
            RelativeLayout.LayoutParams rlpSP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
            rlpSP.addRule(RelativeLayout.BELOW, templateView1.getId());
            mSupportMapFragment.getView().setLayoutParams(rlpSP);
        }

        fabTurnByTurnDirections.bringToFront();
        hScrollViewPlaces.bringToFront();
        mapSize.bringToFront();
        rgViews.bringToFront();
        editTextPlaceMap.bringToFront();
    }

    private @NonNull RvAdapter getRvAdapter() {
        ArrayList<Integer> arrayListNearArrayDrawables = new ArrayList<>();
        arrayListNearArrayDrawables.add(R.drawable.temple);
        arrayListNearArrayDrawables.add(R.drawable.atm);
        arrayListNearArrayDrawables.add(R.drawable.airport);
        arrayListNearArrayDrawables.add(R.drawable.bar);
        arrayListNearArrayDrawables.add(R.drawable.hospital);
        arrayListNearArrayDrawables.add(R.drawable.mall);
        arrayListNearArrayDrawables.add(R.drawable.movietheater);
        arrayListNearArrayDrawables.add(R.drawable.restaurant);
        arrayListNearArrayDrawables.add(R.drawable.school);
        arrayListNearArrayDrawables.add(R.drawable.trainstation);
        arrayListNearArray = new ArrayList<String>(
                Arrays.asList("Edu", "Health", "Movie", "Train", "Plane", "Food", "Drink", "Malls", "Temple", "ATM"));
        return new RvAdapter(this, arrayListNearArray, arrayListNearArrayDrawables, this);
    }


    private void nativeAd() {


        adLoader1 = new AdLoader.Builder(this, prodFlag ? "ca-app-pub-6424332507659067/2788119941" : "ca-app-pub-3940256099942544/2247696110").forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

            private ColorDrawable background;

            @Override
            public void onNativeAdLoaded(NativeAd unifiedNativeAd) {

                NativeTemplateStyle styles = new
                        NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();

                templateView1.setStyles(styles);
                templateView1.setNativeAd(unifiedNativeAd);
                adLoaded1 = true;
                // Showing a simple Toast message to user when Native an ad is Loaded and ready to show
                //        Toast.makeText(MainActivity.this, "Native Ad is loaded ,now you can show the native ad  ", Toast.LENGTH_LONG).show();
            }

        }).build();


        showNativeAd1();

    }


    private void showNativeAd1() {
        if (adLoaded1) {
            templateView1.setVisibility(View.VISIBLE);
            // Showing a simple Toast message to user when an Native ad is shown to the user
            //       Toast.makeText(MainActivity.this, "Native Ad  is loaded and Now showing ad  ", Toast.LENGTH_LONG).show();
            //     showNativeAd();

        } else {
            //Load the Native ad if it is not loaded
            loadNativeAd1();
            // Showing a simple Toast message to user when Native ad is not loaded
            //     Toast.makeText(MainActivity.this, "Native Ad is not Loaded ", Toast.LENGTH_LONG).show();

        }


        //Load the Native ad if it is not loaded

        // Showing a simple Toast message to user when Native ad is not loaded
        //     Toast.makeText(MainActivity.this, "Native Ad is not Loaded ", Toast.LENGTH_LONG).show();


    }

    private void loadNativeAd1() {
        // Creating  an Ad Request
        AdRequest adRequest = new AdRequest.Builder().build();

        // load Native Ad with the Request
        adLoader1.loadAd(adRequest);

        // Showing a simple Toast message to user when Native an ad is Loading
        //     Toast.makeText(MainActivity.this, "Native Ad is loading ", Toast.LENGTH_LONG).show();
    }




  /*  private void showBannerAd() {

        bannerAdView = findViewById(R.id.adview_banner);
        bannerAdView.bringToFront();
//        adView.setAdSize(AdSize.BANNER);
 //       adView.setAdUnitId(prodFlag ? "ca-app-pub-6424332507659067/6907316671" : "ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);

    }*/

    private void listViewPlacesDialog() {

        final ArrayAdapter<String> arrayAdapter = (new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                arrayListPlaces));

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Select a Place to View - ")
                .setIcon(R.drawable.street_icon)
                .setPositiveButton("Search",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                                editTextPlaceStreet.setVisibility(View.VISIBLE);
                                editTextPlaceStreet.setText("");
                                editTextPlaceStreet.requestFocus();

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(editTextPlaceStreet, InputMethodManager.SHOW_IMPLICIT);
                                makeToast("Search a place, you'd like to see!");
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }
                )
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        makeToast(arrayListPlaces.get(position));
                        mStreetViewPanorama.setPosition(arrayListPlacesLatLng.get(position));
                        mSupportStreetViewPanoramaFragment.getView().setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        loadFS();
                    }
                })
                .create().show();
    }

    private void searchPlace(String string) {

        Geocoder coder = new Geocoder(appContext);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(string, 1);

            if (address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
                mStreetViewPanorama.setPosition(p1);
            } else {
                makeToast("no place found, check the spelling maybe!");
            }
        } catch (IOException ex) {

            makeToast("Ex - " + ex);
            ex.printStackTrace();
        }


    }

    private void loadFS() {

        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, prodFlag ? getString(R.string.actual_intr_ad_id) : "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        makeToast("AdLoaded");
                        mInterstitialAd = interstitialAd;
                        showFS();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                        makeToast("FtL - " + loadAdError.getMessage());
                    }
                });
    }


    private void showFS() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }


    public void loadFSR() {
        adRequest = new AdRequest.Builder().build();

        RewardedInterstitialAd.load(MainActivity.this, "ca-app-pub-6424332507659067/5482828095",
                adRequest, new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                makeToast("Ad was clicked.");
                                rewardedInterstitialAd.show(/* Activity */ MainActivity.this,/*
    OnUserEarnedRewardListener */ MainActivity.this);
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                rewardedInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                rewardedInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad showed fullscreen content.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        makeToast("onAdFailedToLoad - " + loadAdError);
                        rewardedInterstitialAd = null;
                    }
                });
    }


    private void InitRoutes(final LatLng srcLatLng, final LatLng destLatLng) {

        destLocation = destLatLng;
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(srcLatLng);
            builder.include(destLatLng);
            LatLngBounds bounds = builder.build();
            int padding = 450; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            MyGmap.moveCamera(cu);
        }


        String strTransportMode = "TransportMode.DRIVING";

        String serverKey = "AIzaSyB4hJ-5vcOeTOsAiK8CpQ5uPD4D7LPArIE";
        GoogleDirection.withServerKey(serverKey)
                .from(srcLatLng)
                .to(destLatLng)
                .transitMode(strTransportMode)
                .transportMode(strTransportMode)
                .execute(new DirectionCallback() {
                    //    public TextView dynTextView;

                    @SuppressLint({"ResourceAsColor", "RestrictedApi"})
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        // Do something here

                        String status = direction.getStatus();
                        if (status.equals(RequestResult.OK)) {
                            // Do something
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);

                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getApplicationContext(), directionPositionList, 5, Color.BLUE);
                            polyLineRoute = MyGmap.addPolyline(polylineOptions);


                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distance = distanceInfo.getText();
                            String duration = durationInfo.getText();

                            Snackbar.make(findViewById(R.id.rl_main), "Distance - " + distance + "\n Duration, generally - " + duration, Snackbar.LENGTH_LONG).show();


                            fabTurnByTurnDirections.setVisibility(View.VISIBLE);


                        } else if (status.equals(RequestResult.NOT_FOUND)) {
                            // Do something
                            makeToast("fails :/ ");
                        }


                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });

        //     }

    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onDestroy() {
        super.onDestroy();

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);


    }

    private void getTimeandDate() {

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int min = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);

        if (hour == 0) {
            hour = 12;
        }

        final int date = c.get(Calendar.DATE);
        int Month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        mTime = hour + ":" + min + ":" + seconds;

        mDate = date + "/" + Month + "/" + year;

    }


    private void getLastKnownLocation() {
        com.belaku.naveenprakash.npstreetmap.MyLocation.LocationResult locationResult = new com.belaku.naveenprakash.npstreetmap.MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                processLocation(location);
                locationUpdates();

           //     Toast.makeText(getApplicationContext(), location.getLatitude() + " : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }


    public void showReminderAlertDialog(LatLng latLng) {


        AddReminderDialog addReminderDialog = new AddReminderDialog(MainActivity.this, latLng);
        addReminderDialog.getWindow().setGravity(Gravity.CENTER);
        addReminderDialog.show();

    }


    private void processLocation(Location location) {

        // Logic to handle location object
        MyLocation = location;


        addressLatitude = MyLocation.getLatitude();
        addressLongitude = MyLocation.getLongitude();

        Log.d("severe", "Into onSuccess location != null getLastKnownLocation" + addressLatitude + " - " + addressLongitude);

        mSupportMapFragment.getMapAsync(MainActivity.this);


        if (!streetPicSet || (Math.round(streetViewPos.latitude * 100) / 100.0 != Math.round(addressLatitude * 100) / 100.0)) {
            makeToast("settingStreetPic");
            mSupportStreetViewPanoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {

                @Override
                public void onStreetViewPanoramaReady(final StreetViewPanorama streetViewPanorama) {

                    mStreetViewPanorama = streetViewPanorama;
                    enableStreetFeatures(mStreetViewPanorama);

                    streetViewPos = new LatLng(addressLatitude, addressLongitude);
                    streetViewPanorama.setPosition(streetViewPos, 50000);
                    rgViews = (RadioGroup) findViewById(R.id.rg_views);


                    mapSize.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SuspiciousIndentation")
                        @Override
                        public void onClick(View v) {
                            MyGmap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                @Override
                                public void onMapLongClick(@NonNull LatLng latLng) {
                                    showReminderAlertDialog(latLng);
                                }
                            });
                            fsMap();
                        }
                    });

                    rgViews.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            if (checkedId == R.id.rb_dark) {  //dark
                                MyGmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                MyGmap.setMapStyle(
                                        MapStyleOptions.loadRawResourceStyle(
                                                getApplicationContext(), R.raw.dark_style));
                            } else if (checkedId == R.id.rb_retro) {  //retro
                                MyGmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                MyGmap.setMapStyle(
                                        MapStyleOptions.loadRawResourceStyle(
                                                getApplicationContext(), R.raw.norm_style));
                            } else if (checkedId == R.id.rb_sat) {  //retro
                                MyGmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                MyGmap.setMapStyle(
                                        MapStyleOptions.loadRawResourceStyle(
                                                getApplicationContext(), R.raw.gray_style));
                            }
                        }
                    });


                    streetHandler.postDelayed(new Runnable() {
                        public void run() {
                            //do something
                            mStreetViewPanoramaCamera =
                                    new StreetViewPanoramaCamera.Builder()
                                            .zoom(mStreetViewPanorama.getPanoramaCamera().zoom)
                                            .tilt(mStreetViewPanorama.getPanoramaCamera().tilt)
                                            .bearing(mStreetViewPanorama.getPanoramaCamera().bearing - 60)
                                            .build();
                            mStreetViewPanorama.animateTo(mStreetViewPanoramaCamera, 1000);
                            streetHandler.postDelayed(this, streetDelay);

                            final Handler handler = new Handler();
                            final int delay = 1000; // 1000 milliseconds == 1 second


                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (mStreetViewPanorama.getLocation() != null) {
                                        streetPicSet = true;
                                        handler.removeCallbacks(this);
                                        if (!aPlaceSearched) {
                                            TxPlaceDetails.setText("a Place, nearby...\n\n" + (String.valueOf(getAddress(mStreetViewPanorama.getLocation().position.latitude, mStreetViewPanorama.getLocation().position.longitude).get(0).getAddressLine(0).toString())));
                                            pd.dismiss();
                                        } else
                                            TxPlaceDetails.setText("a Place, searched...\n\n" + (String.valueOf(getAddress(mStreetViewPanorama.getLocation().position.latitude, mStreetViewPanorama.getLocation().position.longitude).get(0).getAddressLine(0).toString())));
                                    } else {
                                        delayCount++;
                                        if (delayCount == 1)
                                            pd.setMessage("fetching Location.");
                                        else if (delayCount == 2)
                                            pd.setMessage("fetching Location..");
                                        else if (delayCount == 3)
                                            pd.setMessage("fetching Location...");
                                        else if (delayCount == 4)
                                            pd.setMessage("fetching Location....");
                                        else if (delayCount == 5)
                                            pd.setMessage("fetching Location.....");
                                        else if (delayCount == 6)
                                            pd.setMessage("fetching Location......");
                                        else if (delayCount == 7)
                                            pd.setMessage("fetching Location.......");
                                        else if (delayCount == 8)
                                            pd.setMessage("fetching Location........");
                                        else if (delayCount == 9) {
                                            streetPicSet = false;
                                            pd.setMessage("Poor Connectivity, please wait for some more time OR try again, later");
                                            TxPlaceDetails.setText("No Pic found for this Place!");
                                        }
                                        handler.postDelayed(this, delay);
                                    }
                                }
                            }, delay);
                        }
                    }, streetDelay);

                    imgbtn_street_expand_contract.bringToFront();
                }
            });
        }


        //    makeToast("Latitude - " + location.getLatitude() + "\n Longitude - " + location.getLongitude());

        //yet2implUpdates


    }

    @SuppressLint("MissingPermission")
    private void locationUpdates() {


   //     makeToast("Rqstg L Updates!");
        //Instantiating the Location request and setting the priority and the interval I need to update the location.
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setSmallestDisplacement(5);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//instantiating the LocationCallBack
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    makeToast(MessageFormat.format("Lat: {0} Long: {1} Accuracy: {2}", location.getLatitude(),
                            location.getLongitude(), location.getAccuracy()));
                    processLocation(location);
                }
            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void enableStreetFeatures(StreetViewPanorama mStreetViewPanorama) {
        mStreetViewPanorama.setUserNavigationEnabled(true);
        mStreetViewPanorama.setStreetNamesEnabled(true);
        mStreetViewPanorama.setZoomGesturesEnabled(true);
        mStreetViewPanorama.setPanningGesturesEnabled(true);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOB_BG_P: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    {
                        makeToast("P Grantd!");
                        LocationServices.getGeofencingClient(MainActivity.this).addGeofences(geofencingRequest, pendingIntentGeoFence)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        makeToast("GeoF added!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        makeToast("GeoFence Error - " + e);
                                    }
                                });

                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public static void makeToast(String s) {
        Toast.makeText(appContext, s, Toast.LENGTH_SHORT).show();
        Log.d("nLog - ", s);
        //      Snackbar.make(getWindow().getDecorView().getRootView(), s + "\n \n \n", Snackbar.LENGTH_SHORT).show();
    }

    @SuppressLint({"MissingPermission", "ResourceAsColor", "PotentialBehaviorOverride"})
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        //    makeToast("onMapReady");
        MyGmap = googleMap;

        MyGmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (marker.getTitle() != null)
                    if (!marker.getTitle().startsWith("R")) {
                        makeToast(marker.getTitle());
                        if (polyLineRoute != null)
                            polyLineRoute.remove();
                        InitRoutes(mLatLng, marker.getPosition());
                    } else {
                        //      addrs.setTextSize(20);
                        String textMsg = "Sending from Street ... \n " + mDate + "\t - \t" + mTime + "\n I'm @ \n" + mAddress;
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, textMsg);
                        sendIntent.setType("text/plain");

                        if (sendIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(sendIntent);
                        }

                    }
                return true;
            }
        });

        for (int i = 0; i < reminders.size(); i++) {
            String strTitle = reminders.get(i).getStringTask().length() > 7 ? reminders.get(i).getStringTask().substring(0, 7) + "..," : reminders.get(i).getStringTask().substring(0, reminders.get(i).getStringTask().length()) + "..,";
            markerOptionsGeoFence = new MarkerOptions().position(reminders.get(i).getLatLng()).icon(BitmapDescriptorFactory.fromBitmap(new IconGenerator(MainActivity.this).makeIcon(strTitle)));
            markerOptionsGeoFence.title(reminders.get(i).getStringTask());
            markerGeoFence = MyGmap.addMarker(markerOptionsGeoFence);

            CircleOptions circleOptionsGoeFence = new CircleOptions();
            circleOptionsGoeFence.center(reminders.get(i).getLatLng());
            circleOptionsGoeFence.radius(250.0);
            circleOptionsGoeFence.strokeColor(R.color.colorAccent);
            circleOptionsGoeFence.fillColor(R.color.colorPrimary);
            circleOptionsGoeFence.strokeWidth(5);
            MyGmap.addCircle(circleOptionsGoeFence);
        }

        MyGmap.setMyLocationEnabled(true);
        MyGmap.setBuildingsEnabled(true);
        MyGmap.getUiSettings().setTiltGesturesEnabled(true);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.norm_style));

            if (!success) {
                makeToast("Style parsing failed.");
            }
        } catch (Exception e) {
            makeToast("Can't find style. Error: " + e);
        }
        //  MyGmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.norm_style));
        //   MyGmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLatLng = new LatLng(addressLatitude, addressLongitude);


        if (getAddress(addressLatitude, addressLongitude) != null)
            if (getAddress(addressLatitude, addressLongitude).get(0) != null) {
                if (Build.VERSION.SDK_INT >= 35) {
                    mAddress = getAddress(addressLatitude, addressLongitude).get(0).getAddressLine(0);

                    mAddresses = mAddress.split(",");
                }

                addPresentMarker();

                mapSize.bringToFront();

            } else
                makeToast("Failed to get address for Latitude - " + addressLatitude + " and Longitude - " + addressLongitude);

    }

    private void addPresentMarker() {
        BitmapDescriptor icon = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            IconGenerator icnGenerator = new IconGenerator(this);
            // Bitmap bmp = icnGenerator.makeIcon(Html.fromHtml("<b><font color=\"#000000\">" + mAddresses[0] + mAddresses[1] + mAddresses[2] + "\n" + mAddresses[3] + mAddresses[4] + "</font></b>"));
            Bitmap bmp = icnGenerator.makeIcon(Html.fromHtml("<span style=\"color: #000000\"><b>" + mAddresses[0] + "," + mAddresses[1] + "," + mAddresses[2] + "," + "<br> &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; " + mAddresses[3] + "," + mAddresses[4] + "</b></span>"));
            icon = BitmapDescriptorFactory.fromBitmap(bmp);
        }
        markerOptions = new MarkerOptions()
                .position(mLatLng).icon(icon);

        //    marker = googleMap.addMarker(markerOptions);
        markerAddress = MyGmap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(mLatLng).
                tilt(55).
                zoom(15).
                bearing(0).
                build();

        MyGmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    public static List<Address> getAddress(double currentlatitude, double currentlongitude) {

        Geocoder gcd = new Geocoder(appContext);
        Locale.getDefault();
        try {
            addresses = gcd.getFromLocation(currentlatitude, currentlongitude, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(appContext, "GCD - IOException \n " + e.toString(), Toast.LENGTH_LONG).show();
        }

        return addresses;
    }


    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onClick(int pos) {
        makeToast("click : " + pos + " --- value - " + arrayListNearArray.get(pos));
    }


    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        makeToast("User earned reward.");
        // TODO: Reward the user!
    }

    public void POIclick(View view) {
        makeToast(((TextView) view).getText().toString());

        MyGmap.clear();
        addPresentMarker();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                getPlaces(((TextView) view).getText().toString());
            }
        });

        thread.start();

    }


    private void getPlaces(String strPlaceType) {

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/")
                // on below line we are calling add Converter
                // factory as GSON converter factory.
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NearbyPlacesPojo> call = retrofitAPI.getPlaces("nearby", mLatLng.latitude + "," + mLatLng.longitude, strPlaceType);
        call.enqueue(new Callback<NearbyPlacesPojo>() {

            @Override
            public void onResponse(Call<NearbyPlacesPojo> call, retrofit2.Response<NearbyPlacesPojo> response) {
                assert response.body() != null;
                placesNearby = response.body().results;

                addNearbyPlacesMarkers(response.body().results);

            }

            @Override
            public void onFailure(Call<NearbyPlacesPojo> call, Throwable t) {
                // displaying an error message in toast
                Toast.makeText(MainActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNearbyPlacesMarkers(ArrayList<Places> nPlaces) {

        MyGmap.clear();

        addPresentMarker();

        for (int i = 0; i < nPlaces.size(); i++) {
            LatLng latLng = new LatLng(nPlaces.get(i).geocodes.main.latitude, nPlaces.get(i).geocodes.main.longitude);
            //   Bitmap icon = new IconGenerator(MainActivity.this).makeIcon(nPlaces.get(i).name);
            BitmapDescriptor icon = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

                IconGenerator icnGenerator = new IconGenerator(this);
                //   icnGenerator.setBackground(getResources().getDrawable(R.drawable.border));

                Bitmap bmp = icnGenerator.makeIcon(Html.fromHtml("<b><font color=\"#000000\">" + nPlaces.get(i).name + "</font></b>"));
                icon = BitmapDescriptorFactory.fromBitmap(bmp);
            }
            MarkerOptions markerOptionsNearby = new MarkerOptions().position(latLng).icon(icon);
            markerOptionsNearby.title(nPlaces.get(i).name);
            MyGmap.addMarker(markerOptionsNearby);
        }

        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(mLatLng).
                tilt(0).
                zoom(15).
                bearing(0).
                build();

        MyGmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }


}




