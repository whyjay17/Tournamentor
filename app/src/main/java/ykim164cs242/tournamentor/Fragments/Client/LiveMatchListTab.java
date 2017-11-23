package ykim164cs242.tournamentor.Fragments.Client;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.List;

import ykim164cs242.tournamentor.Adapter.Client.MatchListAdapter;
import ykim164cs242.tournamentor.ListItem.MatchListItem;
import ykim164cs242.tournamentor.R;

/**
 * The LiveMatchListTab class represents the tab fragment that displays the LIVE match list.
 * It only fetches the data with "isLive = true" from the Firebase real-time database
 * and displays it in the ListView.
 */
public class LiveMatchListTab extends Fragment {

    ListView matchListView;

    private MatchListAdapter adapter;
    private List<MatchListItem> matchListItems;

    // Storages for parsed data
    private List<String> matchIDList;
    private List<String> fieldNameList;
    private List<String> gameTimeList;
    private List<String> gameDateList;
    private List<String> teamAList;
    private List<Integer> scoreAList;
    private List<String> teamBList;
    private List<Integer> scoreBList;
    private List<Boolean> isLiveList;
    private List<Boolean> isStarredList;

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tournamentReference = rootReference.child("Tournaments");
    DatabaseReference matchReference = tournamentReference.child("Test Tournament").child("Matches");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_match_list, container, false);
        matchListView = (ListView) view.findViewById(R.id.match_list);

        matchListItems = new ArrayList<>();
        matchIDList = new ArrayList<>();
        fieldNameList = new ArrayList<>();
        gameTimeList = new ArrayList<>();
        gameDateList = new ArrayList<>();
        teamAList = new ArrayList<>();
        scoreAList = new ArrayList<>();
        teamBList = new ArrayList<>();
        scoreBList = new ArrayList<>();
        isLiveList = new ArrayList<>();
        isStarredList = new ArrayList<>();

        adapter = new MatchListAdapter(getContext(), matchListItems);
        matchListView.setAdapter(adapter);

        return view;

    }

    /**
     * Fetches the data from the real-time database, stores into the pre-initialized storages,
     * and displays in the ListView. The onDataChange function runs everytime the data is
     * changed in the real-time database.
     */
    @Override
    public void onStart() {
        super.onStart();

        matchReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clearCurrentList();

                // Fires every single time the channelReference updates in the Real-time DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    matchListItems.clear();

                    // ID Format: Date + TeamA + vs + TeamB
                    matchIDList.add(snapshot.child("gameDate").getValue().toString() + " "
                            + snapshot.child("teamA").getValue().toString()
                            + " vs " + snapshot.child("teamB").getValue().toString());

                    fieldNameList.add(snapshot.child("fieldName").getValue().toString());
                    gameTimeList.add(snapshot.child("gameTime").getValue().toString());
                    gameDateList.add(snapshot.child("gameDate").getValue().toString());
                    teamAList.add(snapshot.child("teamA").getValue().toString());
                    scoreAList.add(Integer.parseInt(snapshot.child("scoreA").getValue().toString()));
                    teamBList.add(snapshot.child("teamB").getValue().toString());
                    scoreBList.add(Integer.parseInt(snapshot.child("scoreB").getValue().toString()));
                    isLiveList.add((boolean)snapshot.child("isLive").getValue());
                    isStarredList.add((boolean)snapshot.child("isStarred").getValue());
                }

                for(int i = 0; i < fieldNameList.size(); i++) {

                    // Fetches ONLY the match with "isLive = true"
                    if(isLiveList.get(i) == true) {
                        matchListItems.add(new MatchListItem(matchIDList.get(i), fieldNameList.get(i), gameTimeList.get(i), gameDateList.get(i), teamAList.get(i),
                                teamBList.get(i),scoreAList.get(i),scoreBList.get(i), isLiveList.get(i), isStarredList.get(i)));
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * clearCurrentList clears items inside the data storage for data-redrawing
     */
    public void clearCurrentList() {

        matchIDList.clear();
        fieldNameList.clear();
        gameTimeList.clear();
        gameDateList.clear();
        teamAList.clear();
        scoreAList.clear();
        teamBList.clear();
        scoreBList.clear();
        isLiveList.clear();
        isStarredList.clear();
    }

}
