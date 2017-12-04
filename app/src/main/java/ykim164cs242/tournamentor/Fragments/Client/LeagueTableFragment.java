package ykim164cs242.tournamentor.Fragments.Client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ykim164cs242.tournamentor.Activity.Client.ClientMainActivity;
import ykim164cs242.tournamentor.Adapter.Client.LeagueStandingListAdapter;
import ykim164cs242.tournamentor.Adapter.Client.TeamListAdapter;
import ykim164cs242.tournamentor.ListItem.LeagueTableItem;
import ykim164cs242.tournamentor.ListItem.ScoreTableItem;
import ykim164cs242.tournamentor.ListItem.TeamListItem;
import ykim164cs242.tournamentor.R;

/**
 * Fragment for the league table. To be implemented in Week 2
 */
public class LeagueTableFragment extends Fragment {

    ListView leagueTableListView;

    private LeagueStandingListAdapter adapter;
    private List<LeagueTableItem> leagueTableItemList;

    // Storages for parsed data from the real-time database
    private List<Integer> rankList;
    private List<String> teamNameList;
    private List<Integer> gamesPlayedList;
    private List<Integer> winsList;
    private List<Integer> drawsList;
    private List<Integer> lossesList;
    private List<Integer> goalScoredList;
    private List<Integer> goalAgainstList;
    private List<Integer> goalDifferenceList;
    private List<Integer> pointsList;

    // Firebase Database references.
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tournamentReference;
    DatabaseReference teamReference;
    DatabaseReference gameReference;

    String deviceID = ClientMainActivity.passedInDeviceID;
    String channelID = ClientMainActivity.passedInChannelID;
    String tournamentName = ClientMainActivity.passedInTournamentName;

    public LeagueTableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_standings, container, false);

        leagueTableListView = (ListView) view.findViewById(R.id.standing_listview);

        leagueTableItemList = new ArrayList<>();
        rankList = new ArrayList<>();
        teamNameList = new ArrayList<>();
        gamesPlayedList = new ArrayList<>();
        winsList = new ArrayList<>();
        drawsList = new ArrayList<>();
        lossesList = new ArrayList<>();
        goalScoredList = new ArrayList<>();
        goalAgainstList = new ArrayList<>();
        goalDifferenceList = new ArrayList<>();
        pointsList = new ArrayList<>();

        tournamentReference = rootReference.child("Channels").child(channelID).child("tournaments").child(tournamentName);
        teamReference = tournamentReference.child("teams");
        gameReference = rootReference.child("Channels").child(channelID).child("tournaments").child(tournamentName).child("games");

        adapter = new LeagueStandingListAdapter(getContext(), leagueTableItemList);
        leagueTableListView.setAdapter(adapter);

        return view;//inflater.inflate(R.layout.fragment_standings, container, false);
    }

    /**
     * Fetches the data from the real-time database, stores into the pre-initialized storages,
     * and displays in the ListView. The onDataChange function runs everytime the data is
     * changed in the real-time database.
     */
    @Override
    public void onStart() {
        super.onStart();

        teamReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Clear the storages for redrawing of the ListView
                rankList.clear();
                teamNameList.clear();
                gamesPlayedList.clear();
                winsList.clear();
                drawsList.clear();
                lossesList.clear();
                goalScoredList.clear();
                goalAgainstList.clear();
                goalDifferenceList.clear();
                pointsList.clear();

                // Fires every single time the channelReference updates in the Real-time DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    leagueTableItemList.clear();

                    int wins = Integer.parseInt(snapshot.child("wins").getValue().toString());
                    int draws = Integer.parseInt(snapshot.child("draws").getValue().toString());
                    int losses = Integer.parseInt(snapshot.child("losses").getValue().toString());
                    int goalScored = Integer.parseInt(snapshot.child("goalScored").getValue().toString());
                    int goalAgainst = Integer.parseInt(snapshot.child("goalAgainst").getValue().toString());
                    int goalDifference = Integer.parseInt(snapshot.child("goalDifference").getValue().toString());
                    int point = Integer.parseInt(snapshot.child("point").getValue().toString());

                    teamNameList.add(snapshot.child("teamName").getValue().toString());
                    rankList.add(0);
                    gamesPlayedList.add(Integer.parseInt(snapshot.child("gamesPlayed").getValue().toString()));
                    winsList.add(wins);
                    drawsList.add(draws);
                    lossesList.add(losses);
                    goalScoredList.add(goalScored);
                    goalAgainstList.add(goalAgainst);
                    goalDifferenceList.add(goalDifference);
                    pointsList.add(point); //(Integer.parseInt(snapshot.child("point").getValue().toString()));
                }

                List<LeagueTableItem> tempLeagueTableItem = new ArrayList<LeagueTableItem>();

                for(int i = 0; i < teamNameList.size(); i++) {
                    tempLeagueTableItem.add(new LeagueTableItem(i + 1, teamNameList.get(i), gamesPlayedList.get(i), winsList.get(i),
                            drawsList.get(i), lossesList.get(i), goalScoredList.get(i), goalAgainstList.get(i), goalDifferenceList.get(i), pointsList.get(i)));
                }

                // sorts the tempLeagueTableItem based on numGoals

                sortLeagueTable(tempLeagueTableItem);

                // Giving the right Rank for the sorted items

                int currRank = 1;

               for(LeagueTableItem p : tempLeagueTableItem) {
                   leagueTableItemList.add(new LeagueTableItem(currRank, p.getTeamName(), p.getGamesPlayed(), p.getWins(), p.getDraws(), p.getLosses(), p.getGoalScored(), p.getGoalAgasint(), p.getGoalDifference(), p.getPoints()));
                   currRank++;
               }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * sortLeagueTable sorts the LeagueTableItem object
     * based on one of its fields (points in this case).
     * If the points are equal, it then sorts based on
     * the goalDifference.
     */
    public static void sortLeagueTable(List<LeagueTableItem> table) {
        Collections.sort(table, new Comparator<LeagueTableItem>(){
            public int compare(LeagueTableItem o1, LeagueTableItem o2){
                int pts = o2.getPoints() - o1.getPoints();
                if(o2.getPoints() == o1.getPoints()) {
                    return (-1 * o1.getGoalDifference()) - (-1 * o2.getGoalDifference());
                }
                return pts;
            }
        });
    }

}
