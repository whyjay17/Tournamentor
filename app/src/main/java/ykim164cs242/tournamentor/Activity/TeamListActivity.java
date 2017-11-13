package ykim164cs242.tournamentor.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ykim164cs242.tournamentor.Adapter.ChannelListAdapter;
import ykim164cs242.tournamentor.Adapter.TeamListAdapter;
import ykim164cs242.tournamentor.ListItem.ChannelListItem;
import ykim164cs242.tournamentor.R;
import ykim164cs242.tournamentor.ListItem.TeamListItem;

public class TeamListActivity extends AppCompatActivity {

    ListView teamListView;

    private TeamListAdapter adapter;
    private List<TeamListItem> teamListItems;

    // Storages for parsed JSON data (repoName, userName, description of repositories)
    private List<String> teamNameList;
    private List<String> foundationYearList;
    private List<String> captainNameList;

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tournamentReference = rootReference.child("Tournaments");
    DatabaseReference teamReference = tournamentReference.child("Test Tournament").child("Teams");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        teamListView = (ListView) findViewById(R.id.team_listview);

        teamListItems = new ArrayList<>();
        teamNameList = new ArrayList<>();
        foundationYearList = new ArrayList<>();
        captainNameList = new ArrayList<>();

        adapter = new TeamListAdapter(this, teamListItems);
        teamListView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        teamReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                teamNameList.clear();
                foundationYearList.clear();
                captainNameList.clear();

                // Fires every single time the channelReference updates in the Real-time DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    teamListItems.clear();
                    teamNameList.add(snapshot.child("Name").getValue().toString());
                    foundationYearList.add(snapshot.child("Foundation").getValue().toString());
                    captainNameList.add(snapshot.child("Captain").getValue().toString());
                }

                for(int i = 0; i < teamNameList.size(); i++) {
                    teamListItems.add(new TeamListItem(i, teamNameList.get(i), foundationYearList.get(i), captainNameList.get(i)));
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
