package ykim164cs242.tournamentor.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ykim164cs242.tournamentor.Adapter.Admin.TournamentListAdapter;
import ykim164cs242.tournamentor.InformationStorage.TournamentInfo;
import ykim164cs242.tournamentor.ListItem.TournamentListItem;
import ykim164cs242.tournamentor.R;

/**
 * TeamListActivity represents a screen of displaying participating teams of the tournament.
 * The team information is fetched from the Firebase real-time database and displayed
 * in the ListView of the teams.
 */
public class AdminTournamentListActivity extends AppCompatActivity {

    ListView tournamentListView;

    private FirebaseUser firebaseUser;

    private TournamentListAdapter adapter;
    private List<TournamentListItem> tournamentListItems;

    private Button addButton;
    private TextView emptyText;

    // Storages for parsed data from the real-time database
    private List<String> tournamentNameList;
    private List<String> termList;

    // Firebase Database references.
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tournamentReference = rootReference.child("Tournaments");
    DatabaseReference channelsReference = rootReference.child("Channels");

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tournament_list);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, AdminLoginActivity.class));
        }

        firebaseUser = firebaseAuth.getCurrentUser();
        //firebaseUser.getEmail();

        //saveTournamentInfo(firebaseUser, "Test Tournament", "Dec 10 - 14");

        tournamentListView = (ListView) findViewById(R.id.manage_tournament_listview);

        tournamentListItems = new ArrayList<>();
        tournamentNameList = new ArrayList<>();
        termList = new ArrayList<>();

        addButton = (Button) findViewById(R.id.add_tournament_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add tournament dialog
                //saveTournamentInfo(firebaseUser, "test", "test");
                Intent addTournamentIntent = new Intent(AdminTournamentListActivity.this, AdminAddTournamentActivity.class);
                addTournamentIntent.putExtra("channelID", firebaseUser.getUid());
                startActivity(addTournamentIntent);
            }
        });

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

        DatabaseReference theChannelReference = channelsReference.child(firebaseUser.getUid()).child("tournaments");

        theChannelReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Clear the storages for redrawing of the ListView
                tournamentNameList.clear();
                termList.clear();

                // Fires every single time the channelReference updates in the Real-time DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    tournamentListItems.clear();
                    tournamentNameList.add(snapshot.child("name").getValue().toString());
                    termList.add(snapshot.child("term").getValue().toString());
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

    private void saveTournamentInfo(final FirebaseUser firebaseUser, String name, String term) {
        //TODO: Change term to Date
        final TournamentInfo tournamentInfo =  new TournamentInfo(name, term);
        DatabaseReference tournament = rootReference.child("Tournament");
        tournament.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // users -> githubID : User Profile Data
                String channelName = firebaseUser.getUid();
                //rootReference.child("Channels").child(channelName).setValue(tournamentInfo);
                rootReference.child("Channels").child(channelName).child("Tournaments").setValue(tournamentInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
