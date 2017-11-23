package ykim164cs242.tournamentor.Activity.Client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ykim164cs242.tournamentor.Activity.Admin.AdminLoginActivity;
import ykim164cs242.tournamentor.Adapter.Admin.TournamentListAdapter;
import ykim164cs242.tournamentor.InformationStorage.TournamentInfo;
import ykim164cs242.tournamentor.ListItem.ChannelListItem;
import ykim164cs242.tournamentor.ListItem.TournamentListItem;
import ykim164cs242.tournamentor.R;

/**
 * TeamListActivity represents a screen of displaying participating teams of the tournament.
 * The team information is fetched from the Firebase real-time database and displayed
 * in the ListView of the teams.
 */
public class ClientTournamentListActivity extends AppCompatActivity {

    ListView tournamentListView;

    private TournamentListAdapter adapter;
    private List<TournamentListItem> tournamentListItems;

    private String passedDataFromChannelSelection;
    // Storages for parsed data from the real-time database
    private List<String> tournamentNameList;
    private List<String> termList;

    // Firebase Database references.
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference channelsReference = rootReference.child("Channels");
    DatabaseReference referenceWithID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tournament_list);

        // Receiving data from the SelectChannel Activity
        try {
            Intent intent = getIntent();
            passedDataFromChannelSelection = intent.getStringExtra("channelID");

        } catch(Exception e) {
            e.printStackTrace();
        }

        referenceWithID = channelsReference.child(passedDataFromChannelSelection);

        tournamentListView = (ListView) findViewById(R.id.select_tournament_listview);

        tournamentListItems = new ArrayList<>();
        tournamentNameList = new ArrayList<>();
        termList = new ArrayList<>();

        adapter = new TournamentListAdapter(this, tournamentListItems);
        tournamentListView.setAdapter(adapter);

    }

    /**
     * Fetches the data from the real-time database, stores into the pre-initialized storages,
     * and displays in the ListView. The onDataChange function runs everytime the data is
     * changed in the real-time database.
     */
    @Override
    protected void onStart() {
        super.onStart();

        channelsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Clear the storages for redrawing of the ListView
                tournamentNameList.clear();
                termList.clear();


                // Fires every single time the channelReference updates in the Real-time DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    tournamentListItems.clear();
                    tournamentNameList.add(snapshot.child("Tournaments").child("name").getValue().toString());
                    termList.add(snapshot.child("Tournaments").child("term").getValue().toString());

                }

                for(int i = 0; i < tournamentNameList.size(); i++) {
                    tournamentListItems.add(new TournamentListItem(i, tournamentNameList.get(i), termList.get(i)));
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
