package ykim164cs242.tournamentor.Activity.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ykim164cs242.tournamentor.Adapter.Admin.AdminMatchListAdapter;
import ykim164cs242.tournamentor.Adapter.Client.MatchListAdapter;
import ykim164cs242.tournamentor.ListItem.AdminMatchListItem;
import ykim164cs242.tournamentor.ListItem.MatchListItem;
import ykim164cs242.tournamentor.R;

public class AdminMatchListActivity extends AppCompatActivity {


    ListView matchListView;

    private AdminMatchListAdapter adapter;
    private List<AdminMatchListItem> adminMatchListItems;

    // Storages for parsed JSON data (repoName, userName, description of repositories)
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

    // Firebase database reference
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tournamentReference = rootReference.child("Tournaments");
    DatabaseReference matchReference = tournamentReference.child("Test Tournament").child("Matches");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_match_list);

        matchListView = (ListView) findViewById(R.id.admin_match_listview);

        adminMatchListItems = new ArrayList<>();
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

        adapter = new AdminMatchListAdapter(this, adminMatchListItems);
        matchListView.setAdapter(adapter);
    }
}
