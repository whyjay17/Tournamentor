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

import ykim164cs242.tournamentor.Adapter.SectionsPageAdapter;
import ykim164cs242.tournamentor.Fragments.MatchListTab;
import ykim164cs242.tournamentor.R;

public class UserMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private SectionsPageAdapter pageAdapter;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

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
            // Handle the camera action

        } else if (id == R.id.nav_team_list) {
            Intent teamListIntent = new Intent(UserMainActivity.this, TeamListActivity.class);
            startActivity(teamListIntent);
        } else if (id == R.id.nav_league_status) {

            AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_league_info, null);

            //Define views inside the dialog layout

            TextView textView = (TextView) dialogView.findViewById(R.id.textView16);

            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();

            //startActivity(new Intent(UserMainActivity.this, LeagueInfoPopUp.class));
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
        adapter.addFragment(new MatchListTab(), "Live");
        adapter.addFragment(new MatchListTab(), "Favorite");
        adapter.addFragment(new MatchListTab(), "Data");
        viewPager.setAdapter(adapter);
    }

}
