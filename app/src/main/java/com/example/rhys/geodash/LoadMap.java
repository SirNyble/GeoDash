package com.example.rhys.geodash;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by Kelsey on 3/31/2016.
 */
public class LoadMap extends AppCompatActivity {

    private Button contBtn;
    private RecyclerView mRecyclerView;
    private Firebase myFirebaseRef;
    private ArrayList<MapModel> mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        mModel = new ArrayList<MapModel>();

        // Get a reference to the RecyclerView and configure it
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Setup Firebase
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://burning-inferno-6101.firebaseio.com/");
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                int mapCount = (int) snapshot.child("Maps").getChildrenCount();
                for (int i = 0; i < mapCount; i++) {
                    MapModel post = snapshot.child("Maps").child("" + i).getValue(MapModel.class);
                    mModel.add(post);
                }
                LoadDataTask task = new LoadDataTask();
                task.execute(mModel);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        contBtn = (Button) findViewById(R.id.contBtn);
        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoadMap.this, Map.class);
                startActivity(i);
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


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<MapModel> set;

        public MyAdapter(ArrayList<MapModel> dataset) {
            set = dataset;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView;

            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.map_layout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final MapModel entry = set.get(position);

            holder.mTextView.setText(entry.getMapName());

            holder.mTextView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    final int pos = position;
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MapDetail.class);
                    intent.putExtra(MapDetail.NAME, entry.getMapName());
                    intent.putExtra(MapDetail.TIME, entry.getTimeLimit());
                    intent.putExtra(MapDetail.NUMRIDDLES, entry.getNumRiddles());
                    intent.putExtra(MapDetail.POSITION, pos);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return set.size();
        }
    }

    public class LoadDataTask extends AsyncTask<ArrayList<MapModel>, Void, ArrayList<MapModel>> {
        private ArrayList<MapModel> mapList = new ArrayList<MapModel>();

        @Override
        protected ArrayList<MapModel> doInBackground(ArrayList<MapModel>... params) {
            mapList = params[0];
            return mapList;
        }

        protected void onPostExecute(ArrayList<MapModel> result) {
            RecyclerView.Adapter mAdapter = new MyAdapter(result);
            mRecyclerView.setAdapter(mAdapter);
        }
    }


}

