package com.example.rhys.geodash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Kelsey on 4/2/2016.
 */
public class MapDetail extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String TIME = "time";
    public static final String NUMRIDDLES = "NUMRIDDLES";
    public static final String POSITION = "POSITION";

    private String mMapName = "";
    private int mNumRiddles = 0;
    private int mTimeLimit = 0;
    private int mPos = 0;

    private Button mPlayBtn;
    private Firebase myFirebaseRef;
    private ArrayList<User> mUserModel;

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
            mPos = (int) bundleExtras.get(POSITION);
        }

        TextView defT = (TextView) findViewById(R.id.time);
        defT.setText(mTimeLimit + " minutes");

        TextView rid = (TextView) findViewById(R.id.numRiddles);
        rid.setText(mNumRiddles + " riddles");

        mPlayBtn = (Button) findViewById(R.id.playBtn);
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapDetail.this, Map.class);
                i.putExtra(MapDetail.POSITION, mPos);
                i.putExtra("timeLimit", mTimeLimit);
                startActivity(i);
            }
        });

        // Setup Firebase
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://burning-inferno-6101.firebaseio.com/");
        Query queryRef = myFirebaseRef.orderByChild("score");
        mUserModel = new ArrayList<User>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle(mMapName);
        bar.setDisplayHomeAsUpEnabled(true);

        final Button high = (Button) findViewById(R.id.high);
        high.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String msg = "";
                for(int i = 0; i < mUserModel.size(); i++)
                {
                    String user = mUserModel.get(i).getName();
                    long score = mUserModel.get(i).getScore();
                    msg +=  " - " + user + ": " + score + "\n";
                }

                AlertDialog alertDialog = new AlertDialog.Builder(MapDetail.this).create();
                alertDialog.setTitle("High Scores");
                alertDialog.setMessage(msg);

                alertDialog.setButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });
                alertDialog.show();
            }
        });


        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                //myFirebaseRef.child("Scores").child("Maps").child("" + (mMapId)).child("Users").child("" + usrCount);
                //User oldUser = snapshot.child("Scores").child("Maps").child("" + (mMapId)).child("Users").child("" + usrCount).getValue(User.class);
                int usrCount = (int) snapshot.child("Scores").child("Maps").child("" + (mPos)).child("Users").getChildrenCount();
                for (int i = 0; i < usrCount; i++) {
                    User post = snapshot.child("Scores").child("Maps").child("" + (mPos)).child("Users").child("" + i).getValue(User.class);
                    Log.d("HALP", post.toString());
                    mUserModel.add(post);
                }
                Log.d("HALP", mUserModel.toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
