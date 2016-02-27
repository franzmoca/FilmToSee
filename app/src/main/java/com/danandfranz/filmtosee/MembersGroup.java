package com.danandfranz.filmtosee;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;


public class MembersGroup extends AppCompatActivity {
    JSONObject groupData;




    public MembersGroup() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.members_group);

        try {
            groupData = new JSONObject(getIntent().getStringExtra("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //TOOLBAR

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMembersGroup);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        try {
            ab.setTitle(groupData.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //END OF TOOLBAR SETTINGS



    }



}
