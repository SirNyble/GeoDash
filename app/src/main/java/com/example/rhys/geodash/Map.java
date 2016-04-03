package com.example.rhys.geodash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.location.Location.distanceBetween;

/**
 * Created by Kelsey on 2/15/2016.
 */
public class Map extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    private int mMapId;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "DEBUG: ";
    private LocationRequest mLocationRequest;
    private Marker mCurLocation;
    private int mScore;
    private TextView mScoreText;
    private Button mGuessButton = null;
    private Button mRiddleButton = null;
    private boolean mIsFirstUpdate = true;

    private Firebase myFirebaseRef;

    private ArrayList<RiddleLocation> mRiddleLocations;
    private int mRound;

    public int mMapMinutes = 10;
    private TextView mTimeLeftView;

    public Map() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundleExtras = getIntent().getExtras();

        if(bundleExtras != null)
        {
            mMapId = (int) bundleExtras.get("POSITION");
            mMapMinutes = (int) bundleExtras.get("timeLimit");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mScore = 0;
        mRound = 0;
        mRiddleLocations = new ArrayList<RiddleLocation>();

        // Setup Firebase
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://burning-inferno-6101.firebaseio.com/");

        mScoreText = (TextView) findViewById(R.id.scoreText);
        mTimeLeftView = (TextView) findViewById( R.id.timeView );

        //setup google api
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        // Create the LocationRequest object
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10 * 1000);      // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mRiddleButton = (Button) findViewById(R.id.riddleButton);
        mRiddleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showRiddle(mRound);
            }
        });
        mRiddleButton.setEnabled(false);

        mGuessButton = (Button) findViewById(R.id.guessButton);
        mGuessButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Get the Current location
                double curLat = mCurLocation.getPosition().latitude;
                double curLong = mCurLocation.getPosition().longitude;
                //Get the Acutal location
                double actLat = mRiddleLocations.get(mRound).getLatitude();
                double actLong = mRiddleLocations.get(mRound).getLongitude();


                float[] results = new float[1];
                distanceBetween(curLat, curLong, actLat, actLong, results);
                int tmpScore = (int)(1000 - results[0]);
                mScore += tmpScore >= 0 ? tmpScore : 0;

                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(curLat, curLong), new LatLng(actLat, actLong))
                        .width(5)
                        .color(Color.RED));

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(new LatLng(curLat, curLong))
                        .include(new LatLng(actLat, actLong)).build();

                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
                String scoreTxt = "" + mScore;
                mScoreText.setText(scoreTxt);

                mRound++;
                if(mRound >= mRiddleLocations.size())
                {
                    showGameFinishAlert();
                }
                else
                {
                    showRiddle(mRound);
                }

            }
        });
        mGuessButton.setEnabled(false);
    }

    public void showGameFinishAlert()
    {
        mGuessButton.setEnabled(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
        builder.setTitle("Game Done")
                .setMessage("Score: " + mScore);
        builder.setPositiveButton("Add Highscore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Map.this);
                builder2.setTitle("Add Highscore")
                        .setMessage("Score: " + mScore + "\n" + "Name:");
                LayoutInflater inflater = Map.this.getLayoutInflater();
                View v_iew=inflater.inflate(R.layout.high_score, null) ;
                final EditText uNameText = (EditText) v_iew.findViewById(R.id.usernameScore);
                builder2.setView(v_iew)

                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int id) {

                                // sign in the user ...
                                myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        // do some stuff once

                                        Log.d("Blah", "SNAPSHOT: " + snapshot.child("Users").getValue());

                                        //String riddleLocations = snapshot.child("Fredericton").child("Location").getValue().toString();
                                        Log.d("Blah", "CHILDREN COUNT: " + snapshot.child("Users").getChildrenCount());
                                        //myFirebaseRef.child("Maps").child(mModel.getMapName());


                                        String usrName = uNameText.getText().toString();
                                        int usrCount = (int) snapshot.child("Scores").child("Maps").child("" + (mMapId)).getChildrenCount();
                                        Firebase mapRef = myFirebaseRef.child("Scores").child("Maps").child("" + (mMapId)).child(usrName);
                                        User oldUser = snapshot.child("Scores").child("Maps").child("" + (mMapId)).child(usrName).getValue(User.class);

                                        User usr = new User(usrName, mScore);
                                        if(oldUser == null || oldUser.getScore() < usr.getScore())
                                            mapRef.setValue(usr);

                                        /*Firebase alanRef = myFirebaseRef.child("users").child("alanisawesome");

                                        alanRef.setValue(alan);*/

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Map.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                AlertDialog alertDialog2 = builder2.create();

                alertDialog2.show();
            }
        });
        builder.setNegativeButton("Home", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(Map.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadRiddleLocations()
    {
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int locationCount = (int) snapshot.child("Maps").child(Integer.toString(mMapId)).child("riddleLocation").getChildrenCount();
                for (int i = 0; i < locationCount; i++) {
                    RiddleLocation curLocation = new RiddleLocation();
                    DataSnapshot riddleLocations = snapshot.child("Maps").child(Integer.toString(mMapId)).child("riddleLocation").child(Integer.toString(i));

                    curLocation.setName(snapshot.child("Maps").child(Integer.toString(mMapId)).child("mapName").getValue().toString());
                    curLocation.setRiddle(riddleLocations.child("riddle").getValue().toString());
                    curLocation.setLatitude(Double.parseDouble(riddleLocations.child("latitude").getValue().toString()));
                    curLocation.setLongitude(Double.parseDouble(riddleLocations.child("longitude").getValue().toString()));
                    mRiddleLocations.add(curLocation);
                }
                showRiddle(0);

                //Declare the timer
              /*  Timer t = new Timer();
                //Set the schedule function and rate
                t.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                TextView tv = (TextView) findViewById(R.id.timeView);
                                tv.setText(String.valueOf(mMapMinutes) + ":" + String.valueOf(mMapSeconds));
                                mMapSeconds -= 1;

                                if(mMapSeconds == 0 && mMapMinutes <= 0)
                                {
                                    showGameFinishAlert();

                                }
                                if (mMapSeconds == 0) {
                                    tv.setText(String.valueOf(mMapMinutes) + ":" + String.valueOf(mMapSeconds));

                                    mMapSeconds = 60;
                                    mMapMinutes = mMapMinutes - 1;
                                }

                            }
                        });
                    }
                }, 0, 1000);*/

                CountDownTimer t1 = new CountDownTimer(mMapMinutes*60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mTimeLeftView.setText("" +new SimpleDateFormat("mm:ss:SS").format(new Date( millisUntilFinished)));
                    }

                    public void onFinish() {
                        mTimeLeftView.setText("0:00!");
                        showGameFinishAlert();
                    }
                }.start();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void showRiddle(int riddleIndex)
    {
        String riddleMsg = mRiddleLocations.get(riddleIndex).getRiddle();
        String riddleTitle = mRiddleLocations.get(riddleIndex).getName();
        AlertDialog alertDialog = new AlertDialog.Builder(Map.this).create();
        alertDialog.setTitle(riddleTitle);
        alertDialog.setMessage(riddleMsg);

        alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });

        alertDialog.show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mCurLocation = mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("I am here!"));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);
        mMap.animateCamera(zoom);

        loadRiddleLocations();

    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
        mRiddleButton.setEnabled(true);
        mGuessButton.setEnabled(true);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        mCurLocation.setPosition(latLng);

        if(mIsFirstUpdate)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);
            mMap.animateCamera(zoom);
            mIsFirstUpdate = false;
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
        updateUI(location.getLatitude(), location.getLongitude());
    }

    private void updateUI(double lat, double lon) {
        mScoreText.setText("" + mScore);
    }

}
