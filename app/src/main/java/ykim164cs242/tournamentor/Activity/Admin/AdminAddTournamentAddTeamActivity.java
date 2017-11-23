package ykim164cs242.tournamentor.Activity.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import ykim164cs242.tournamentor.Activity.Client.ClientMainActivity;
import ykim164cs242.tournamentor.Adapter.Client.TeamListAdapter;
import ykim164cs242.tournamentor.InformationStorage.AdminUserInfo;
import ykim164cs242.tournamentor.InformationStorage.ChannelInfo;
import ykim164cs242.tournamentor.InformationStorage.TeamInfo;
import ykim164cs242.tournamentor.InformationStorage.TournamentInfo;
import ykim164cs242.tournamentor.ListItem.TeamListItem;
import ykim164cs242.tournamentor.R;

/**
 * TeamListActivity represents a screen of displaying participating teams of the tournament.
 * The team information is fetched from the Firebase real-time database and displayed
 * in the ListView of the teams.
 */
public class AdminAddTournamentAddTeamActivity extends AppCompatActivity {

    private String name;
    private String date;

    private Button addTeamButton;
    private Button submitButton;
    private ListView teamListView;

    private String channelName;
    private String tournamentName;

    private TeamListAdapter adapter;
    private List<TeamListItem> teamListItems;

    // Storages for parsed data from the real-time database
    private List<String> teamNameList;
    private List<String> foundationYearList;
    private List<String> captainNameList;
    private List<TeamInfo> teamInfoList;

    // Firebase Database references.
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();

    DatabaseReference teamReference;
    DatabaseReference tournamentReference;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_tournament_add_team);

        try {
            Intent intent = getIntent();

            // Tournament name and term

            name = intent.getStringExtra("inputName");
            date = intent.getStringExtra("inputDate");
        } catch(Exception e) {
            e.printStackTrace();
        }

        teamInfoList = new ArrayList<>();

        addTeamButton = (Button) findViewById(R.id.add_team_button);
        submitButton = (Button) findViewById(R.id.submit_addtournament_button);
        teamListView = (ListView) findViewById(R.id.participating_teams_list);

        teamListItems = new ArrayList<>();
        teamNameList = new ArrayList<>();
        foundationYearList = new ArrayList<>();
        captainNameList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        channelName = firebaseUser.getUid();

        tournamentReference = rootReference.child("Channels").child(channelName).child("tournaments");

        adapter = new TeamListAdapter(this, teamListItems);
        teamListView.setAdapter(adapter);

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TeamInfo teamInfo = new TeamInfo("temp", "Dreuoit2", "2013", "Park");
                teamInfoList.add(teamInfo);
                rootReference.child("Channels").child(channelName).child("tournaments").child(name).child("teams").child(name).setValue(teamInfo);


                // Add Team To DB and ListView

         //       AlertDialog.Builder builder = new AlertDialog.Builder(AdminAddTournamentAddTeamActivity.this);
         //       View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_participating_teams, null);
         //       Button dialogSubmitButton = (Button) dialogView.findViewById(R.id.add_team_submit);
         //       final EditText dialogInputTeamName = (EditText) dialogView.findViewById(R.id.input_team_name);
         //       final EditText dialogInputFoundationYear = (EditText) dialogView.findViewById(R.id.input_foundation_year);
         //       final EditText dialogInputCaptainName = (EditText) dialogView.findViewById(R.id.input_captain_name);
//
//
         //       dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
         //           @Override
         //           public void onClick(View v) {
//
         //               //Define views inside the dialog layout
         //               TeamInfo teamInfo = new TeamInfo("temp", dialogInputTeamName.getText().toString(),
         //                     dialogInputFoundationYear.getText().toString(), dialogInputCaptainName.getText().toString());
         //               teamInfoList.add(teamInfo);
         //               rootReference.child("Channels").child(channelName).child("tournaments").child(name).child("teams").child("temp").setValue(teamInfo);
//
         //           }
         //       });
//
         //       builder.setView(dialogView);
         //       AlertDialog dialog = builder.create();
         //       dialog.show();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                TournamentInfo tournamentInfo = new TournamentInfo(name, date, teamInfoList, null);

                rootReference.child("Channels").child(firebaseUser.getUid()).child("tournaments").child(name).setValue(tournamentInfo);

            }
        });

    }

    /**
     * Fetches the data from the real-time database, stores into the pre-initialized storages,
     * and displays in the ListView. The onDataChange function runs everytime the data is
     * changed in the real-time database.
     */
    @Override
    protected void onStart() {
        super.onStart();
        teamReference = tournamentReference.child(name).child("teams");
        teamReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Clear the storages for redrawing of the ListView
                teamNameList.clear();
                foundationYearList.clear();
                captainNameList.clear();

                // Fires every single time the channelReference updates in the Real-time DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    teamListItems.clear();
                    teamNameList.add(snapshot.child("teamName").getValue().toString());
                    foundationYearList.add(snapshot.child("foundationYear").getValue().toString());
                    captainNameList.add(snapshot.child("captainName").getValue().toString());
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
