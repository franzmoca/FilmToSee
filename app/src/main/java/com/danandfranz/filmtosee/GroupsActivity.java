package com.danandfranz.filmtosee;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

public class GroupsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // Session Manager Class
    SessionManager session;
    private static final String TAG = "GroupsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        //CONTROLLO LOGIN
        session = new SessionManager(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);
        String rid = user.get(SessionManager.KEY_RID);
        Log.d(TAG,"RID: "+rid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.d(TAG, "Name " + name + " Email " + email);
        try {
            setAvatar(name, email);
        }catch (Exception e){

        }
        // FINE LOGIN: Alla fine del login sostituisco avatar e stringhe
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //CARTE

        ArrayList<Card> cards = new ArrayList<Card>();

        //Create a Card
        Card card = new Card(this);
        //Create a CardHeader
        CardHeader header = new CardHeader(this);
        header.setTitle("CineForum");
        //Add Header to card
        card.addCardHeader(header);

        cards.add(card);

        CardArrayRecyclerViewAdapter mCardArrayAdapter = new CardArrayRecyclerViewAdapter(this, cards);

        //Staggered grid view
        CardRecyclerView mRecyclerView = (CardRecyclerView) this.findViewById(R.id.carddemo_recyclerview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set the empty view
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }
    }

    private void setAvatar(String name, String email) {
//        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_groups, null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView=navigationView.getHeaderView(0);

        ImageView avatar = (ImageView) headerView.findViewById(R.id.avatar);
        TextView username = (TextView) headerView.findViewById(R.id.user);
        TextView mail = (TextView) headerView.findViewById(R.id.email);
        username.setText(name);
        mail.setText(email);
        //controlla se c'è l'avatar sul db e semmai lo scarica
        if(!avatarPresent()){
            final Resources res = getResources();
            final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
            final LetterTileProvider tileProvider = new LetterTileProvider(this);
            final RoundedAvatarDrawable letterTile = tileProvider.getCircleLetterTile(name, "key", tileSize, tileSize);
            avatar.setImageDrawable(letterTile);
        }
        //Aggiunge CARTE

    }

    private boolean avatarPresent() {
        //interra funziona php avatar e lo scarica semmai ?!
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.groups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_group) {
            // Handle the camera action
        } else if (id == R.id.invite_friend) {

        } else if (id == R.id.settings) {
            Intent intent = new Intent(this, InsideGroupActivity.class);
            startActivity(intent);

        } else if (id == R.id.faq) {

        } else if (id == R.id.logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
