package com.danandfranz.filmtosee;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class InsideGroupActivity extends AppCompatActivity {


    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter adapter;
    private ArrayList<Game> games;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);

        settingDummyData();
        adapter = new CoverFlowAdapter(this, games);
        coverFlow.setAdapter(adapter);
        coverFlow.setOnScrollPositionListener(onScrollListener());


    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollListener() {
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                Log.v("MainActiivty", "position: " + position);
            }

            @Override
            public void onScrolling() {
                Log.i("MainActivity", "scrolling");
            }
        };
    }

    private void settingDummyData() {
        games = new ArrayList<>();
        games.add(new Game(R.mipmap.assassins_creed, "Assassin Creed 3"));
        games.add(new Game(R.mipmap.avatar_3d, "Avatar 3D"));
        games.add(new Game(R.mipmap.call_of_duty_black_ops_3, "Call Of Duty Black Ops 3"));
        games.add(new Game(R.mipmap.dota_2, "DotA 2"));
        games.add(new Game(R.mipmap.halo_5, "Halo 5"));
        games.add(new Game(R.mipmap.left_4_dead_2, "Left 4 Dead 2"));
        games.add(new Game(R.mipmap.starcraft, "StarCraft"));
        games.add(new Game(R.mipmap.the_witcher_3, "The Witcher 3"));
        games.add(new Game(R.mipmap.tomb_raider, "Tom raider 3"));
        games.add(new Game(R.mipmap.need_for_speed_most_wanted, "Need for Speed Most Wanted"));
    }

   public void showDetailes(View v) {
        RelativeLayout r = (RelativeLayout) findViewById(R.id.layoutDetailes);
       RelativeLayout y = (RelativeLayout) findViewById(R.id.layoutComments);

        y.setVisibility(View.INVISIBLE);
        r.setVisibility(View.VISIBLE);

    }

   public void showComments(View v) {
       RelativeLayout r = (RelativeLayout) findViewById(R.id.layoutDetailes);
       RelativeLayout y = (RelativeLayout) findViewById(R.id.layoutComments);

        r.setVisibility(View.INVISIBLE);
        y.setVisibility(View.VISIBLE);

    }

}

