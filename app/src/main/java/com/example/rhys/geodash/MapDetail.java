package com.example.rhys.geodash;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by Kelsey on 4/2/2016.
 */
public class MapDetail extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String TIME = "time";
    public static final String NUMRIDDLES = "NUMRIDDLES";

    private String mMapName = "";
    private int mNumRiddles = 0;
    private int mTimeLimit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);

        Bundle bundleExtras = getIntent().getExtras();

        if(bundleExtras != null)
        {
            mMapName =(String) bundleExtras.get(NAME);
            mNumRiddles = (int) bundleExtras.get(NUMRIDDLES);
            mTimeLimit = (int) bundleExtras.get(TIME);
        }

        TextView defT = (TextView) findViewById(R.id.time);
        defT.setText(mTimeLimit + " minutes");

        TextView rid = (TextView) findViewById(R.id.numRiddles);
        rid.setText(mNumRiddles + " riddles");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle(mMapName);
        bar.setDisplayHomeAsUpEnabled(true);

    }
}
