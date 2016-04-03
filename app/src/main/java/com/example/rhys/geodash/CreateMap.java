package com.example.rhys.geodash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Kelsey on 3/21/2016.
 */
public class CreateMap extends AppCompatActivity {

        private Button contBtn;
        private EditText mMapNameView;
        private EditText mNumRiddlesView;
        private EditText mTimeLimitView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create);

            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

            android.support.v7.app.ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);

            mMapNameView = (EditText) findViewById(R.id.nameField);

            mTimeLimitView = (EditText) findViewById(R.id.timeLimitField);

            mNumRiddlesView = (EditText) findViewById(R.id.numRiddlesField);
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
                    else
                    {
                        Context context = getApplicationContext();
                        CharSequence text = "Error: Make sure all fields are filled out!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
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
