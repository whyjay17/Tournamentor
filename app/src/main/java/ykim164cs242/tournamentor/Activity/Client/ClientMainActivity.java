package ykim164cs242.tournamentor.Activity.Client;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ykim164cs242.tournamentor.Adapter.SectionsPageAdapter;
import ykim164cs242.tournamentor.Fragments.Client.LiveMatchListTab;
import ykim164cs242.tournamentor.Fragments.Client.MatchListTab;
import ykim164cs242.tournamentor.Fragments.Client.StandingsTab;
import ykim164cs242.tournamentor.Fragments.Client.StarredMatchListTab;
import ykim164cs242.tournamentor.R;

/**
 * The ClientMainActivity class represents the main User Interface that utilizes both TabLayout and NavigationDrawer.
 * It handles the select listeners for each item of the NavigationDrawer, and adds fragment tabs into the TabLayout
 * so that the user can move to different tabs with different views.
 */

public class ClientMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private SectionsPageAdapter pageAdapter;

    ViewPager viewPager;

    // Firebase Database Reference

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference channelsReference;
    DatabaseReference tournamentReference;

    // Passed-in data from the SelectChannel Activity. Will be used to reach the right database reference

    public static String passedInChannelID;
    public static String passedInTournamentName;
    public static String passedInDeviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // Receiving data from the SelectChannel Activity
        try {
            Intent intent = getIntent();
            passedInTournamentName = intent.getStringExtra("tournamentName");
            passedInChannelID = intent.getStringExtra("channelID");
            passedInDeviceID = intent.getStringExtra("deviceID");

        } catch(Exception e) {
            e.printStackTrace();
        }

        channelsReference = rootReference.child("Channels").child(passedInChannelID);
        tournamentReference = channelsReference.child("tournaments").child(passedInTournamentName);

        // Fragment Manager for TabLayout

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new MatchListTab());

        // Handles NavigationDrawer

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sets up ViewPager so that it can display different tab fragments

        pageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager((viewPager));

    }

    /**
     * Handles item selection option for each item in the NavigationDrawer
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_select_channel) {

            // Handle the select_channel action: Moves to SelectChannelActivity

            Intent channelIntent = new Intent(ClientMainActivity.this, SelectChannelActivity.class);
            startActivity(channelIntent);

        } else if (id == R.id.nav_team_list) {

            // Handle the nav_team_list action: Moves to TeamListActivity

            Intent teamListIntent = new Intent(ClientMainActivity.this, TeamListActivity.class);
            startActivity(teamListIntent);

        } else if (id == R.id.nav_league_status) {

            // Handle the nav_league_status action: Displays dialog that contains fetched league information from the real-time database


            AlertDialog.Builder builder = new AlertDialog.Builder(ClientMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_league_info, null);

            // Define views inside the dialog layout

            final TextView competitionName = (TextView) dialogView.findViewById(R.id.dialog_comp_name_placeholder);
            final TextView term = (TextView) dialogView.findViewById(R.id.dialog_term_placeholder);
            final TextView hostName = (TextView) dialogView.findViewById(R.id.dialog_host_placeholder);

            // Fetching data from the real-time database

            tournamentReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    competitionName.setText(dataSnapshot.child("name").getValue().toString());
                    term.setText(dataSnapshot.child("term").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();


        } else if (id == R.id.nav_info) {

            // Handle the nav_info action: Displays dialog that contains the information about the app

            AlertDialog.Builder builder = new AlertDialog.Builder(ClientMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_about_info, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (id == R.id.nav_faq) {

            // Handle the nav_faq action: Displays dialog that contains the information about the FAQ

            AlertDialog.Builder builder = new AlertDialog.Builder(ClientMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_faq_info, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_layout);
        drawer.closeDrawer(GravityCompat.START);

        // return false to remove color from selected items
        return false;
    }

    // Adds fragments to SectionsPageAdapter and gives names for the corresponding tab

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MatchListTab(), "Scores");
        adapter.addFragment(new LiveMatchListTab(), "Live");
        adapter.addFragment(new StarredMatchListTab(), "Favorite");
        adapter.addFragment(new StandingsTab(), "Data");
        viewPager.setAdapter(adapter);
    }

}
