package com.example.rhys.geodash;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

        // TODO Get the intent that started this activity, and get the word and definition
        // extras from it.
        String w = getIntent().getStringExtra(NAME);
        String d = getIntent().getStringExtra(TIME);
        String r = getIntent().getStringExtra(NUMRIDDLES);


        // TODO Set the definition TextView to be the definition
        TextView defT = (TextView) findViewById(R.id.time);
        defT.setText(d + " minutes");

        TextView rid = (TextView) findViewById(R.id.numRiddles);
        rid.setText(r + " riddles");

        // TODO Set the title of the action bar to be the word
        //
        // Hint: Note the location of the word (being defined) in the detail activity in
        // the lab exam write-up. This portion of an Activity is the action bar. You can get
        // a reference to the action bar here using getSupportActionBar(). There is a link to
        // documentation on this method in the "Hints" section of the write-up.
        ActionBar bar = getSupportActionBar();
        bar.setTitle(w);

    }
}
