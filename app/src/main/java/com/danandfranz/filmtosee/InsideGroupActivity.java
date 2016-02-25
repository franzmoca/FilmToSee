package com.danandfranz.filmtosee;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.rockerhieu.emojicon.*;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;



public class InsideGroupActivity  extends AppCompatActivity {


    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter adapter;
    private ArrayList<Film> films;


    //SWIPE
    private Toolbar toolbarFragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //END SWIPE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_group);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //TOOLBAR

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGroup);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //END OF TOOLBAR SETTINGS

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //COVER FLOW
                coverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);

                settingDummyData();
                adapter = new CoverFlowAdapter(InsideGroupActivity.this, films);
                coverFlow.setAdapter(adapter);
                coverFlow.setOnScrollPositionListener(onScrollListener());
                //END OF COVER FLOW


            }
        });


        //swipe

        toolbarFragment = (Toolbar) findViewById(R.id.toolbarFragment);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //end swipe
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
        films = new ArrayList<>();
        films.add(new Film(R.mipmap.assassins_creed, "Assassin Creed 3"));
        films.add(new Film(R.mipmap.avatar_3d, "Avatar 3D"));
        films.add(new Film(R.mipmap.call_of_duty_black_ops_3, "Call Of Duty Black Ops 3"));
        films.add(new Film(R.mipmap.dota_2, "DotA 2"));
        films.add(new Film(R.mipmap.halo_5, "Halo 5"));
        films.add(new Film(R.mipmap.left_4_dead_2, "Left 4 Dead 2"));
        films.add(new Film(R.mipmap.starcraft, "StarCraft"));
        films.add(new Film(R.mipmap.the_witcher_3, "The Witcher 3"));
        films.add(new Film(R.mipmap.tomb_raider, "Tom raider 3"));
        films.add(new Film(R.mipmap.need_for_speed_most_wanted, "Need for Speed Most Wanted"));
        films.add(new Film(R.mipmap.addfilmcover, "Add Movie"));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.addUserGroup:
                addMember();
                return true;


            case R.id.membri_gruppo:

               Intent intent = new Intent(this, MembersGroup.class);
                startActivity(intent);
                return true;
            case R.id.lascia_gruppo:
                LeaveGroup();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inside_group, menu);
        return true;
    }

    //SWIPE
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter1 = new ViewPagerAdapter(getSupportFragmentManager());
        adapter1.addFragment(new OneFragment(), "MOVIE DETAILES");
        adapter1.addFragment(new TwoFragment(), "COMMENTS");
        viewPager.setAdapter(adapter1);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    //END SWIPE

    private void addMember() {

        new MaterialDialog.Builder(this)
                .title("Add Member")
                .content("Please insert the username of the user you want to add")
                .theme(Theme.LIGHT)
                .positiveText("Add Member")
                .input("Username", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        if (input.toString().length() > 3) {


                        } else {



                        }
                    }
                }).show();

    }

    private void LeaveGroup() {

        new MaterialDialog.Builder(this)
                .title("Confirm?")
                .content("Are you sure you want to leave and delete this Group?")
                .theme(Theme.LIGHT)
                .positiveText("Yes")
                .negativeText("Cancel")
                .icon(this.getResources().getDrawable(R.drawable.ic_delete_24dp))
                .show();

    }
}
