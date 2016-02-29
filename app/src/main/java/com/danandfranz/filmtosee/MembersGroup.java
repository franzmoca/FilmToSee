package com.danandfranz.filmtosee;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MembersGroup extends AppCompatActivity {
    private static final String TAG = "MembersGroup";
    JSONObject groupData;
    private JSONArray membersJson;


    public MembersGroup() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.members_group);

        try {
            groupData = new JSONObject(getIntent().getStringExtra("json"));
            membersJson = groupData.getJSONArray("members");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,membersJson.toString() );
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
//Members to array
        List<String> members =new ArrayList<>();
        for(int i = 0 ; i<membersJson.length();i++){
            try {
                members.add(membersJson.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ListView membersList = (ListView) findViewById(R.id.listMembers);
        MembersGroupAdapter adapter = new MembersGroupAdapter(this,members);
        membersList.setAdapter(adapter);



    }




}
