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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);

        String w = getIntent().getStringExtra(NAME);
        String d = getIntent().getStringExtra(TIME);
        String r = getIntent().getStringExtra(NUMRIDDLES);

        TextView defT = (TextView) findViewById(R.id.time);
        defT.setText(d + " minutes");

        TextView rid = (TextView) findViewById(R.id.numRiddles);
        rid.setText(r + " riddles");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle(w);
        bar.setDisplayHomeAsUpEnabled(true);

    }
}
