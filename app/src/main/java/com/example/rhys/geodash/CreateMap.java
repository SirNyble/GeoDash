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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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

            holderList = (ListView) findViewById(R.id.holder);
            contBtn = (Button) findViewById(R.id.contBtn);
            contBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItems();
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


       private void addItems()
       {
           EditText name = (EditText) findViewById(R.id.nameField);
           EditText city = (EditText) findViewById(R.id.cityField);
           EditText time = (EditText) findViewById(R.id.timeLimitField);
           CheckBox isPrivate = (CheckBox) findViewById(R.id.isPrivateBox);
           int hold = 0;
           if(isPrivate.isChecked()) hold = 1;
           SQLiteDatabase db = dbHelper.getWritableDatabase();
           ContentValues values = new ContentValues();
           values.put(DBHelper.NAME, name.getText().toString());
           values.put(DBHelper.CITY, city.getText().toString());
           values.put(DBHelper.TIMELIMIT, time.getText().toString());
           values.put(DBHelper.PRIVATE, hold);
           db.insert(
                   DBHelper.TABLE_NAME,
                   null,
                   values);

           display();
       }

        private void display()
        {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.query(DBHelper.TABLE_NAME,
                    DBHelper.COLUMNS, null, new String[] {}, null, null, null);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    CreateMap.this,
                    R.layout.content_layout,
                    c,
                    ITEM_NUM_COLS,
                    new int[] { R.id.name, R.id.city, R.id.time, R.id.isprivate},
                    0);
            holderList.setAdapter(adapter);
        }


}
