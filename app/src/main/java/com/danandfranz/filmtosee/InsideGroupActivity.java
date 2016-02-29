package com.danandfranz.filmtosee;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Field;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class InsideGroupActivity  extends AppCompatActivity {

    private static final String TAG = "InsideGroupActivity" ;

    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter coverFlowAdapter;
    private ArrayList<Film> films;
    public JSONObject groupData;
    private String ridFilm;
    private String ridUser;

    private RelativeLayout content_group_layout;

    OkHttpClient client;
    ProgressDialog progressDialog;
    private ViewPagerAdapter fragmentAdapter;
    SessionManager session;
    RelativeLayout rootlayout;




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
        rootlayout = (RelativeLayout) findViewById(R.id.rootlayoutInside);


        progressDialog = new ProgressDialog(InsideGroupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        ridUser = user.get(SessionManager.KEY_RID);
        content_group_layout=(RelativeLayout) findViewById(R.id.content_group_layout);


        client = new OkHttpClient();

        try {
            groupData = new JSONObject(getIntent().getStringExtra("json"));
                  //setSubtitle(groupData.getJSONArray("members").length() + " members.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        //TOOLBAR

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGroup);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        try {
            ab.setTitle(groupData.getString("name"));
            ab.setSubtitle(groupData.getJSONArray("members").length()+" members");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ///TOOLBAR PER MEMBERLIST
            View.OnClickListener members =new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        memberList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            Field subTitle = Toolbar.class.getDeclaredField("mSubtitleTextView");
            subTitle.setAccessible(true);
            final TextView barTitleView = (TextView) subTitle.get(toolbar);
            barTitleView.setOnClickListener(members);

            Field title = Toolbar.class.getDeclaredField("mTitleTextView");
            title.setAccessible(true);
            final TextView titleView = (TextView) title.get(toolbar);
            titleView.setOnClickListener(members);
            ///FINE TOOLBAR PER MEMBERLIST
        } catch (Exception e) {
            e.printStackTrace();
        }



        //END OF TOOLBAR SETTINGS
        //Scarico i dati json dei film e li aggiungo alla coverflow

        //swipe

        toolbarFragment = (Toolbar) findViewById(R.id.toolbarFragment);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);



        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //end swipe

        try {
            ridFilm = groupData.getString("rid");
            Log.d(TAG,"Rid Group "+getGroupRid()+ " Rid User "+getUserRid());
            progressDialog.show();
            getFilmJsonByGroup(ridFilm,ridUser);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getGroupRid(){
        try {
            return groupData.getString("rid");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getUserRid(){
        return ridUser;

    }

    public String getMemberList(){
        try {
            return groupData.getString("rid");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollListener() {
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position)
            {
                Log.v(TAG, "position: " + position);
                Film film = coverFlowAdapter.getItem(position);
                TabLayout.Tab details = tabLayout.getTabAt(0);
                TabLayout.Tab comments = tabLayout.getTabAt(1);
                Log.d(TAG,""+film.isLiked());

                if(film.isAdd()){

                    if (details != null) {
                        details.setText("ADD FILM");
                    }
                    if (comments != null) {
                        comments.setText("GROUP CHAT");
                    }


                }else{

                    if (details != null) {
                        details.setText("MOVIE DETAILS");
                    }
                    if (comments != null) {
                        comments.setText("COMMENTS");
                    }

                }
                OneFragment one = (OneFragment) fragmentAdapter.getItem(0);
                one.setMovieDetails(film);
            }
            @Override
            public void onScrolling() {
                Log.i(TAG, "scrolling");
            }
        };
    }

    private void settingFilmData(JSONArray jsonObj) throws JSONException {
        films = new ArrayList<>();
        films.add(new Film()); //Aggiungo Film Vuoto
        for(int i = 0;i<jsonObj.length();i++){
            films.add(new Film(jsonObj.getJSONObject(i)));
        }

        //Faccio partire la coverflow

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Creo la coverflow
                RelativeLayout parent = (RelativeLayout) findViewById(R.id.relativeLayout);


                LayoutInflater inflater = (LayoutInflater)   InsideGroupActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.coverflow, parent);
                coverFlow = (FeatureCoverFlow) v.findViewById(R.id.coverflow);
                //COVER FLOW
                //coverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
                Log.d(TAG, "CIAO" + films.size());

                coverFlowAdapter = new CoverFlowAdapter(InsideGroupActivity.this, films);
                coverFlow.setAdapter(coverFlowAdapter);
                coverFlow.setOnScrollPositionListener(onScrollListener());
                //END OF COVER FLOW
                progressDialog.hide();


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addFilmGroup:

                addFilm();
                return true;


            case R.id.addUserGroup:
                addMember();
                return true;

            case R.id.aggiungi_membro:
                addMember();
                return true;

            case R.id.membri_gruppo:
                try {
                    memberList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    public void memberList() throws JSONException {
        Intent intent = new Intent(this, MembersGroup.class);
        intent.putExtra("json", groupData.toString());
        startActivity(intent);

    }
    //SWIPE
    private void setupViewPager(ViewPager viewPager) {
        fragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(new OneFragment(), "MOVIE DETAILS");
        fragmentAdapter.addFragment(new TwoFragment(), "COMMENTS");
        viewPager.setAdapter(fragmentAdapter);
    }

    public JSONObject getGroupData() {
        return groupData;
    }

    public ArrayList<Film> getFilms() {
        return films;
    }
    public void setLikeUnlike(String imdbID , int like, int dislike,String liked,boolean mylike){

        Film film = null;
        int position = 1;
        int e = 0 ;
        boolean loop = true;
        while(loop){
        //for(position = 0 ; position < films.size() ; position++){

             film = films.get(position);

             Log.d(TAG,film.getImdbID());
            if(film.getImdbID().equals(imdbID)) {
                e = position;
                film.setLike(like);
                film.setDislike(dislike);
                film.setIsLiked(liked);
                Log.d(TAG, "" + film.isLiked());
                film.setMyLike(mylike);
                loop = false;
            }
            position++;

            if(position >= films.size()){
                loop = false;
            }
        }

      films.set(e,film);
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

        public CharSequence setPageTitle(int position,String title) {
            return mFragmentTitleList.set(position, title);
        }
    }
    //END SWIPE


    private void LeaveGroup() {

        new MaterialDialog.Builder(this)
                .title("Confirm?")
                .content("Are you sure you want to leave and delete this Group?")
                .theme(Theme.LIGHT)
                .positiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        leaveGroup(getGroupRid(),getUserRid());
                    }
                })
                .negativeText("Cancel")
                .icon(this.getResources().getDrawable(R.drawable.ic_delete_24dp))
                .show();

    }


    private void leaveGroup(String groupRid, String userRid){


        RequestBody body;
        body = new FormBody.Builder()
                .add("get","leaveGroup")
                .add("groupRid",groupRid)
                .add("userRid", userRid)
                .build();
        try {
            Util.post(body,client, new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call , Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    //System.out.println(response.body().toString());
                    try {
                        String json = response.body().string();
                        Log.d(TAG,json);
                        JSONObject jsonObj = new JSONObject(json);
                        String result = jsonObj.getString("result");
                        Log.d(TAG, result);
                        if (result.equalsIgnoreCase("success")) {
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                       /* try {
                            JSONArray values = groupData.getJSONArray(groupData.getString("name"));
                            for (int i = 0; i < values.length(); i++) {
                                JSONObject item = values.getJSONObject(i);
                                String usernameMember = item.getString("name");
                            }
                            Log.d("members", "" + groupData.getJSONArray("members"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                        if (input.toString().length() > 3) {
                            if (true) {
                                //aggiungere controllo se utente già esistente
                                  addMemberToGroup(getGroupRid(), input.toString());
                            }


                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Handle UI here
                                    //findViewById(R.id.loading).setVisibility(View.GONE);

                                    Snackbar.make(rootlayout, "Please insert a username with more than 3 letters", Snackbar.LENGTH_LONG)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    // Perform anything for the action selected
                                                }
                                            }).setDuration(Snackbar.LENGTH_LONG).show();

                                }
                            });

                        }
                    }
                }).show();

    }


    private void addMemberToGroup(String groupRid, String username){



        RequestBody body;
        body = new FormBody.Builder()
                .add("get","addMemberToGroup")
                .add("groupRid",groupRid)
                .add("username", username)
                .build();
        try {
            Util.post(body,client, new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call , Response response) throws IOException {
                    if (!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);

                    }
                    //System.out.println(response.body().toString());
                    try {
                        String json = response.body().string();
                        Log.d(TAG,json);
                        JSONObject jsonObj = new JSONObject(json);
                        String result = jsonObj.getString("result");
                        Log.d(TAG, result);
                        if (result.equalsIgnoreCase("success")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Handle UI here
                                    //findViewById(R.id.loading).setVisibility(View.GONE);

                                    Snackbar.make(rootlayout, "Member added successfully", Snackbar.LENGTH_LONG)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    // Perform anything for the action selected
                                                }
                                            }).setDuration(Snackbar.LENGTH_LONG).show();
                                    restartActivity();

                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Handle UI here
                                //findViewById(R.id.loading).setVisibility(View.GONE);

                                Snackbar.make(rootlayout, "Operation failed", Snackbar.LENGTH_LONG)
                                        .setAction("Try Again", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                addMember();
                                            }
                                        }).setDuration(Snackbar.LENGTH_LONG).show();

                            }
                        });
                    }

                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void getFilmJsonByGroup(String rid, String ridUser) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("get", "filmOfGroup")
                .add("groupRid", rid)
                .add("userrid", ridUser)
                .build();

        Util.post(body, client, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //System.out.println(response.body().toString());
                try {
                    String json = response.body().string();
                    Log.d(TAG, json);
                    JSONArray jsonObj = new JSONArray(json);

                    if (jsonObj.length() >= 0) {
                        settingFilmData(jsonObj);

                    } else {
                        //progressDialog.hide();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addFilm(){
        boolean wrapInScrollView = true;
        final Film[] filmToAdd = new Film[1];
        final MaterialDialog filmdialog = new MaterialDialog.Builder(this)
                .title("Add Film")
                .customView(R.layout.addfilmlayout, wrapInScrollView)
                .theme(Theme.LIGHT)
                .positiveText("Show")
                .negativeText("Close")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DelayAutoCompleteTextView filmTitle = (DelayAutoCompleteTextView) dialog.getCustomView().findViewById(R.id.et_book_title);

                        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(filmTitle.getWindowToken(), 0);
                        if (coverFlow.getScrollPosition() == 1) {
                            coverFlow.scrollToPosition(0);
                        }
                        if (coverFlow.getScrollPosition() != 0) {
                            coverFlow.scrollToPosition(1);
                        }
                        if (filmToAdd[0] != null && filmToAdd[0].getTitle().equalsIgnoreCase(filmTitle.getText().toString())) {
                            OneFragment one = (OneFragment) fragmentAdapter.getItem(0);
                            one.setAddMovieDetails(filmToAdd[0]);


                        } else {
                            if (!isEmpty(filmTitle)) {
                                try {
                                    getFilmByName(filmTitle.getText().toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                })
                .show();

        final DelayAutoCompleteTextView filmTitle = (DelayAutoCompleteTextView) filmdialog.getCustomView().findViewById(R.id.et_book_title);
        filmTitle.setThreshold(3);
        filmTitle.setAdapter(new FilmAutoCompleteAdapter(this)); // 'this' is Activity instance
        filmTitle.setLoadingIndicator(
                (android.widget.ProgressBar) filmdialog.getCustomView().findViewById(R.id.pb_loading_indicator));
        filmTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Film film = (Film) adapterView.getItemAtPosition(position);
                filmTitle.setText(film.getTitle());
                filmToAdd[0] = film;
            }
        });


    }
    public void restartActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void getFilmByName(String name) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("get", "getFilmByName")
                .add("filmName", name)
                .build();

        Util.post(body, client, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //System.out.println(response.body().toString());
                try {
                    final String json = response.body().string();
                    JSONObject filmJson = new JSONObject(json);
                    if(filmJson.getString("Response").equals("True")){
                    final Film film = new Film(filmJson,true);
                    final OneFragment one = (OneFragment) fragmentAdapter.getItem(0);
                    Log.d(TAG, "pd" + film.getTitle());


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            one.setAddMovieDetails(film);
                        }
                    });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(rootlayout, "The film selected doesn't exist!", Snackbar.LENGTH_LONG)
                                        .setAction("Close", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Perform anything for the action selected
                                            }
                                        }).setDuration(Snackbar.LENGTH_LONG).show();

                            }
                        });
                    }
                                    Log.d(TAG, json);
                }

                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
            }

        });

    }



}
