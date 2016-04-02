package com.example.rhys.geodash;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Kelsey on 3/21/2016.
 */
public class CreateMap extends AppCompatActivity {

        private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
        private GoogleMap mMap;
        private GoogleApiClient mGoogleApiClient;

        private LocationRequest mLocationRequest;

        private DBHelper dbHelper;
        private ListView holderList;
        private Button contBtn;
        private Button backBtn;
        private EditText mMapNameView;
        private EditText mNumRiddlesView;
        private EditText mTimeLimitView;

        private static final String[] ITEM_NUM_COLS = { DBHelper.NAME, DBHelper.CITY, DBHelper.TIMELIMIT, DBHelper.PRIVATE };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create);

            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

            // Get a support ActionBar corresponding to this toolbar
            android.support.v7.app.ActionBar ab = getSupportActionBar();

            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);

            dbHelper = new DBHelper(this);

            mMapNameView = (EditText) findViewById(R.id.nameField);

            mTimeLimitView = (EditText) findViewById(R.id.timeLimitField);

            mNumRiddlesView = (EditText) findViewById(R.id.numRiddlesField);

            holderList = (ListView) findViewById(R.id.holder);
            contBtn = (Button) findViewById(R.id.contBtn);
            contBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMapNameView.getText().toString() != ""
                            && mTimeLimitView.getText().toString() != ""
                            && mNumRiddlesView.getText().toString() != "")
                    {
                        Intent createMapCont = new Intent(CreateMap.this, CreateMapCont.class);
                        createMapCont.putExtra("mapName", mMapNameView.getText().toString());
                        createMapCont.putExtra("timeLimit", Integer.parseInt(mTimeLimitView.getText().toString()));
                        createMapCont.putExtra("numRiddles", Integer.parseInt(mNumRiddlesView.getText().toString()));
                        startActivity(createMapCont);
                    }
                }
            });




        }


        @Override
        protected void onResume() {
            super.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();

        }




}
