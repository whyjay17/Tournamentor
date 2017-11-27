package ykim164cs242.tournamentor.Adapter.Admin;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ykim164cs242.tournamentor.Activity.Admin.AdminAddTournamentAddTeamActivity;
import ykim164cs242.tournamentor.InformationStorage.GameInfo;
import ykim164cs242.tournamentor.InformationStorage.PlayerInfo;
import ykim164cs242.tournamentor.ListItem.AdminMatchListItem;
import ykim164cs242.tournamentor.ListItem.AdminTeamListItem;
import ykim164cs242.tournamentor.ListItem.MatchListItem;
import ykim164cs242.tournamentor.ListItem.ScoreTableItem;
import ykim164cs242.tournamentor.R;

/**
 * AdminMatchListAdapter represents an Adapter that can be used in the ListView of matches.
 * It puts in the right data inside the activity_match_list_items.
 */

public class AdminMatchListAdapter extends BaseAdapter{

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String scoredTeam;
    String scorerName;

    HashMap<String, Integer> goalHashMap;

    private Context context;
    private List<AdminMatchListItem> adminMatchList;

    public AdminMatchListAdapter(Context context, List<AdminMatchListItem> adminMatchList) {
        this.context = context;
        this.adminMatchList = adminMatchList;
    }

    @Override
    public int getCount() {
        return adminMatchList.size();
    }

    @Override
    public Object getItem(int position) {
        return adminMatchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Matches each TextView with the corresponding id, and stores the right information
     * by using setText function. It retrieves the correct repositoryName, userName,
     * and description based on the position of the item in the repoList.
     */

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Game ID Formatting
        final String gameID = adminMatchList.get(position).getGameDate() + adminMatchList.get(position).getTeamA() + adminMatchList.get(position).getTeamB();

        // Database references

        final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference liveReference = rootReference.child("Channels").child(firebaseUser.getUid()).child("tournaments")
                .child(adminMatchList.get(position).getTournamentName()).child("games").child(gameID).child("live");
        final DatabaseReference tournamentReference = rootReference.child("Channels").child(firebaseUser.getUid()).child("tournaments")
                .child(adminMatchList.get(position).getTournamentName());
        final DatabaseReference scorerReference = tournamentReference.child("scorers");
        final DatabaseReference gamesReference = tournamentReference.child("games");

        final View view = View.inflate(context, R.layout.activity_admin_match_list_item, null);
        TextView fieldName = (TextView) view.findViewById(R.id.comp_name);
        final TextView gameTime = (TextView) view.findViewById(R.id.game_time);
        TextView gameDate = (TextView) view.findViewById(R.id.game_date);
        final TextView teamA = (TextView) view.findViewById(R.id.team_a);
        final TextView teamB = (TextView) view.findViewById(R.id.team_b);
        TextView scoreA = (TextView) view.findViewById(R.id.score_a);
        TextView scoreB = (TextView) view.findViewById(R.id.score_b);
        Switch liveSwitch = (Switch) view.findViewById(R.id.live_status_switch);

        ImageView deleteButton = (ImageView) view.findViewById(R.id.delete_game_button);
        ImageView editButton = (ImageView) view.findViewById(R.id.edit_game_button);

        // HashMap for counting number of goals of scorers
        goalHashMap = new HashMap<>();

        scorerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Fires every single time the channelReference updates in the Real-time DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String playerName = snapshot.child("playerName").getValue().toString();
                    int goals = Integer.parseInt(snapshot.child("goals").getValue().toString());

                    // Correctly maps the number of goals to a scorer
                    goalHashMap.put(playerName, goals);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { /* BLANK */}
        });

        // Manage buttons

        Button updateScoreButton = (Button) view.findViewById(R.id.update_score_button);
        Button endGameButton = (Button) view.findViewById(R.id.end_game_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootReference.child("Channels").child(firebaseUser.getUid()).child("tournaments")
                        .child(adminMatchList.get(position).getTournamentName()).child("games").child(gameID).getRef().removeValue();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: WEEK3 update score dialog
            }
        });

        updateScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Opens an update score dialog

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View dialogView = inflater.inflate(R.layout.dialog_update_score, null);

                Button udpdateScoreButton = (Button) dialogView.findViewById(R.id.update_score_button);

                RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio_group_scored);
                RadioButton teamAradio = (RadioButton) dialogView.findViewById(R.id.teamA_score);
                RadioButton teamBradio = (RadioButton) dialogView.findViewById(R.id.teamB_score);

                teamAradio.setText(adminMatchList.get(position).getTeamA());
                teamBradio.setText(adminMatchList.get(position).getTeamB());

                // DB reference for scores
                final DatabaseReference gameReference = rootReference.child("Channels").child(firebaseUser.getUid()).child("tournaments")
                        .child(adminMatchList.get(position).getTournamentName()).child("games").child(gameID);

                // Select which team scored

                teamAradio.setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                scoredTeam = adminMatchList.get(position).getTeamA();
                            };
                        }
                );

                teamBradio.setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                scoredTeam = adminMatchList.get(position).getTeamB();
                            }
                        }
                );

                // Input name of the scorer

                final EditText inputScorer = (EditText) dialogView.findViewById(R.id.input_scorer);

                builder.setView(dialogView);
                builder.setTitle("Update Score");
                final AlertDialog dialog = builder.create();
                dialog.show();

                udpdateScoreButton.setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                String scorer = inputScorer.getText().toString();

                                // Update score of team A
                                if(scoredTeam.equalsIgnoreCase(adminMatchList.get(position).getTeamA())) {

                                    final PlayerInfo scorerInfo = new PlayerInfo((inputScorer.getText().toString()) + adminMatchList.get(position).getTeamA(), (inputScorer.getText().toString()), adminMatchList.get(position).getTeamA(), 2);
                                    int currentScore = adminMatchList.get(position).getScoreA();
                                    currentScore++;
                                    gameReference.child("scoreA").setValue(Integer.toString(currentScore));
                                    gameReference.child("scorers").child(scorer).setValue(scorerInfo);

                                    // if the scorer is already in the HashMap -> update number of goals

                                    if(goalHashMap.containsKey(scorer)) {

                                        int currentGoal = goalHashMap.get(scorer);
                                        currentGoal++;
                                        scorerReference.child(adminMatchList.get(position).getTeamA() + scorer).child("goals").setValue(currentGoal);

                                    } else {

                                        // if the scorer is not in the HashMap -> add a new scorer with goal = 1

                                        PlayerInfo newScorer = new PlayerInfo(adminMatchList.get(position).getTeamA() + scorer, scorer, adminMatchList.get(position).getTeamA(), 1);
                                        scorerReference.child(adminMatchList.get(position).getTeamA() + scorer).setValue(newScorer);
                                    }

                                    // Update score of teamB

                                } else if(scoredTeam.equalsIgnoreCase(adminMatchList.get(position).getTeamB())) {

                                    final PlayerInfo scorerInfo = new PlayerInfo((inputScorer.getText().toString()) + adminMatchList.get(position).getTeamB(), (inputScorer.getText().toString()), adminMatchList.get(position).getTeamB(), 1);
                                    int currentScore = adminMatchList.get(position).getScoreB();
                                    currentScore++;
                                    gameReference.child("scoreB").setValue(Integer.toString(currentScore));
                                    gameReference.child("scorers").child(scorer).setValue(scorerInfo);

                                    // if the scorer is already in the HashMap -> update number of goals

                                    if(goalHashMap.containsKey(scorer)) {

                                        int currentGoal = goalHashMap.get(scorer);
                                        currentGoal++;
                                        scorerReference.child(adminMatchList.get(position).getTeamB() + scorer).child("goals").setValue(currentGoal);

                                    } else {

                                        // if the scorer is not in the HashMap -> add a new scorer with goal = 1

                                        PlayerInfo newScorer = new PlayerInfo(adminMatchList.get(position).getTeamB() + scorer, scorer, adminMatchList.get(position).getTeamB(), 1);
                                        scorerReference.child(adminMatchList.get(position).getTeamB() + scorer).setValue(newScorer);
                                    }

                                }

                                dialog.dismiss();

                            };
                        }
                );

                // Determine who is winning. It immediately updates the win, draw, loss since it's showing a live game.

            }
        });

        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // End game: change time to FT (Full Time) and set live to false

                adminMatchList.get(position).setLive(false);
                liveReference.setValue(false);
                gamesReference.child(gameID).child("gameTime").setValue("FT");
                gameTime.setText("FT");
            }
        });

        // Set Texts for TextViews

        fieldName.setText(adminMatchList.get(position).getFieldName());
        gameTime.setText(adminMatchList.get(position).getGameTime());
        gameDate.setText(adminMatchList.get(position).getGameDate());
        teamA.setText(adminMatchList.get(position).getTeamA());
        teamB.setText(adminMatchList.get(position).getTeamB());
        scoreA.setText(Integer.toString(adminMatchList.get(position).getScoreA()));
        scoreB.setText(Integer.toString(adminMatchList.get(position).getScoreB()));

        final DatabaseReference matchReference = tournamentReference.child("Test Tournament").child("Matches").child(adminMatchList.get(position).getGameDate() + " "
                + adminMatchList.get(position).getTeamA() + " vs "
        + adminMatchList.get(position).getTeamB()).child("isStarred");

        // Live Status: Control the switch (set based on live status) Does not reset when changing other switch buttons

        if(adminMatchList.get(position).isLive()) {

            liveSwitch.setChecked(true);

        } else {

            liveSwitch.setChecked(false);

    }

    // Live Status : Blink effect if game is live

        liveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // If game live is checked
                    Toast.makeText(context, "Is live", Toast.LENGTH_SHORT).show();
                    adminMatchList.get(position).setLive(true);
                    liveReference.setValue(true);


                } else {
                    // Pause game
                    Toast.makeText(context, "Is paused", Toast.LENGTH_SHORT).show();
                    //adminMatchList.get(position).setLive(false);
                    liveReference.setValue(false);
                }
            }
        });
        return view;
    }
}
