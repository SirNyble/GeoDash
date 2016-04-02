package com.example.rhys.geodash;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by Kelsey on 3/31/2016.
 */
public class LoadMap extends AppCompatActivity {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private DBHelper dbHelper;
    private ListView holderList;
    private Button contBtn;
    private Button backBtn;
    private static final String[] ITEM_NUM_COLS = { DBHelper.NAME, DBHelper.CITY, DBHelper.TIMELIMIT, DBHelper.PRIVATE };
    private RecyclerView mRecyclerView;

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


        contBtn = (Button) findViewById(R.id.contBtn);
        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoadMap.this, Map.class);
                startActivity(i);
            }
        });
        // Get a reference to the RecyclerView and configure it
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        LoadDataTask task = new LoadDataTask();
        task.execute();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    //TODO - Complete the RecyclerView Adapter
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
        public void onBindViewHolder(ViewHolder holder, int position) {
            // TODO Get the DictionaryEntry at index position in mDataSet
            // You might need to declare this variable as final.
            final MapModel entry = set.get(position);


            // TODO Set the TextView in the ViewHolder (holder) to be the
            // word in this DictionaryEntry
            holder.mTextView.setText(entry.getMapName());

            // TODO Set the onClickListener for the TextView in the ViewHolder (holder) such
            // that when it is clicked, it creates an explicit intent to launch DetailActivity
            // HINT: You will need to put two extra pieces of information in this intent,
            // the word, and its definition

            holder.mTextView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MapDetail.class);
                    intent.putExtra(MapDetail.NAME, entry.getMapName());
                    intent.putExtra(MapDetail.TIME, entry.getmTimeLimit());
                    intent.putExtra(MapDetail.NUMRIDDLES, entry.getNumRiddles());
                    context.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return set.size();
        }
    }

    public class LoadDataTask extends AsyncTask<Void, Void, ArrayList<MapModel>> {
        private ArrayList<MapModel> entryList;

        protected ArrayList<MapModel> doInBackground(Void... params) {
            // TODO Use DataModel to load the data from the JSON assets file
            // and return the ArrayList of DictionaryEntrys
            DataModel model = new DataModel(getApplicationContext());
            entryList = model.getEntries();

            return entryList;
        }

        protected void onPostExecute(ArrayList<MapModel> result) {
            // TODO Use result to set the adapter for the RecyclerView in MainActivity
            RecyclerView.Adapter mAdapter = new MyAdapter(result);
            mRecyclerView.setAdapter(mAdapter);


        }
    }


}

