package com.example.rhys.geodash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by rhys on 02/04/16.
 */
public class CreateMapCont extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "DEBUG: ";// MainActivity.class.getSimpleName();
    private LocationRequest mLocationRequest;
    private boolean mIsFirstUpdate;

    private String mMapName = "";
    private int mNumRiddles = 0;
    private int mTimeLimit = 0;
    private int mCurrentRiddle;
    private TextView mNumRiddleText;
    private EditText mRiddleEdit;
    private Button mNextRiddleBtn;
    private Marker mCurLocation;

    private MapModel mModel;

    private Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_cont);

        // Get a support ActionBar corresponding to this toolbar
       // android.support.v7.app.ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle bundleExtras = i.getExtras();

        if(bundleExtras != null)
        {
            mMapName =(String) bundleExtras.get("mapName");
            mNumRiddles = (int) bundleExtras.get("numRiddles");
            mTimeLimit = (int) bundleExtras.get("timeLimit");
        }
        mModel = new MapModel(mMapName,mTimeLimit, mNumRiddles);

        mIsFirstUpdate = true;
        mCurrentRiddle = 0;

        // Setup Firebase
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://burning-inferno-6101.firebaseio.com/");

        //Setup textViews and EditViews
        mNumRiddleText = (TextView) findViewById(R.id.numRiddleText);
        mRiddleEdit = ((EditText)findViewById(R.id.riddleText));
        mRiddleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
              public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                  if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                      InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                      in.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                  }
                  return false;
              }
          });

        mNumRiddleText.setText("Riddle " + String.valueOf(mCurrentRiddle + 1) + ":");

        mNextRiddleBtn = (Button) findViewById(R.id.nextButton);
        mNextRiddleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String riddleMsg = mRiddleEdit.getText().toString();
                double lat = mCurLocation.getPosition().latitude;
                double lng = mCurLocation.getPosition().longitude;

                RiddleLocation newLoc = new RiddleLocation();
                newLoc.setLatitude(lat);
                newLoc.setLongitude(lng);
                newLoc.setRiddle(riddleMsg);

                mModel.getRiddleLocation().add(newLoc);

                //Show that riddle is added
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Riddle: " + (mCurrentRiddle + 1) + " has been set!"));

                //Create toast to show that riddle is added
                Context context = getApplicationContext();
                CharSequence text = "Riddle: " + (mCurrentRiddle + 1) + " has been added!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //Setup next riddle to add
                mCurrentRiddle++;

                //Check if done adding riddles
                if(mCurrentRiddle == mNumRiddles)
                {
                    myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // do some stuff once

                            Log.d("Blah", "SNAPSHOT: " + snapshot.child("Maps").getValue());

                            //String riddleLocations = snapshot.child("Fredericton").child("Location").getValue().toString();
                            Log.d("Blah", "CHILDREN COUNT: " + snapshot.child("Maps").getChildrenCount());
                            //myFirebaseRef.child("Maps").child(mModel.getMapName());
                            int mapCount = (int) snapshot.child("Maps").getChildrenCount();
                            Firebase mapRef = myFirebaseRef.child("Maps").child("" + (mapCount));
                            mapRef.setValue(mModel);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                   // Firebase mapRef = myFirebaseRef.child("Maps").child(mModel.getMapName());
                    //mapRef.setValue(mModel);

                   // Log.d("DONE", "Finished adding riddles NOw add to firebase");
                   // Toast toast1 = Toast.makeText(context, "Successfully Added Map!", duration);
                   // toast1.show();
                    Log.d("BLH", "AHL");
                    Intent i = new Intent(CreateMapCont.this, MainActivity.class);
                    startActivity(i);
                    Log.d("BLH", "AHL2");
                    //Firebase alanRef = ref.child("users").child("alanisawesome");
                    //User alan = new User("Alan Turing", 1912);
                    //alanRef.setValue(alan);
                    //Firebase cityRef = myFirebaseRef.child("Fredericton").child("Location");

                    /*HashMap<String, Object> riddle = new HashMap<String, Object>();
                    riddle.put("Latitude", "45.946777");
                    riddle.put("Longitude","-66.676234");
                    riddle.put("Riddle Name", "Home");
                    riddle.put("Riddle Message", "After a long day, You'll find me lounging at...");
                    cityRef.setValue(riddle);*/

                }
                else
                {
                    mNumRiddleText.setText("Riddle " + String.valueOf(mCurrentRiddle  + 1) + ":");
                    mRiddleEdit.setText("");
                }

            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //setup google api
        mGoogleApiClient=new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();

        mGoogleApiClient.connect();

        // Create the LocationRequest object
        mLocationRequest=new

        LocationRequest();//LocationRequest.create()

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10*1000);      // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1*1000); // 1 second, in milliseconds

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        //mLatitudeText.setText(String.valueOf(currentLatitude));
        // mLongitudeText.setText(String.valueOf(currentLongitude));

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
       // mCurLocation.setPosition(latLng);

        if(mIsFirstUpdate)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);
            mMap.animateCamera(zoom);
            mIsFirstUpdate = false;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "MAP READY");
        mMap = googleMap;
        //mCurLocation = mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("I am here!"));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(17);
        mMap.animateCamera(zoom);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                if(mCurLocation == null)
                {
                    mCurLocation = mMap.addMarker(new MarkerOptions().position(arg0).title("Riddle: " + mCurrentRiddle + " location!"));
                }
                else
                {
                    mCurLocation.setPosition(arg0);
                }
                Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
            }
        });
    }
}
