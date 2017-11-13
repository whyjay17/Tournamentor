package ykim164cs242.tournamentor.Activity;

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

import org.w3c.dom.Text;

import ykim164cs242.tournamentor.Adapter.SectionsPageAdapter;
import ykim164cs242.tournamentor.Fragments.LiveMatchListTab;
import ykim164cs242.tournamentor.Fragments.MatchListTab;
import ykim164cs242.tournamentor.Fragments.StandingsTab;
import ykim164cs242.tournamentor.Fragments.StarredMatchListTab;
import ykim164cs242.tournamentor.ListItem.MatchListItem;
import ykim164cs242.tournamentor.R;

public class UserMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private SectionsPageAdapter pageAdapter;

    ViewPager viewPager;

    // Firebase Database Reference

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference channelsReference = rootReference.child("Channels");

    String passedDataFromChannelSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        try {
            Intent intent = getIntent();
            passedDataFromChannelSelection = intent.getStringExtra("tournamentName");

        } catch(Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new MatchListTab());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_layout);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager((viewPager));


    }

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
            // Handle the select_channel action

            Intent channelIntent = new Intent(UserMainActivity.this, SelectChannelActivity.class);

            startActivity(channelIntent);

        } else if (id == R.id.nav_team_list) {

            Intent teamListIntent = new Intent(UserMainActivity.this, TeamListActivity.class);
            startActivity(teamListIntent);

        } else if (id == R.id.nav_league_status) {

            DatabaseReference competitionReference = channelsReference.child(passedDataFromChannelSelection + " Channel");

            AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_league_info, null);

            //Define views inside the dialog layout

            final TextView competitionName = (TextView) dialogView.findViewById(R.id.dialog_comp_name_placeholder);
            final TextView term = (TextView) dialogView.findViewById(R.id.dialog_term_placeholder);
            final TextView hostName = (TextView) dialogView.findViewById(R.id.dialog_host_placeholder);

            competitionReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    competitionName.setText(dataSnapshot.child("Name").getValue().toString());
                    term.setText(dataSnapshot.child("Term").getValue().toString());
                    hostName.setText(dataSnapshot.child("Host").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();


        } else if (id == R.id.nav_info) {


            AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_about_info, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (id == R.id.nav_faq) {

            AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
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
