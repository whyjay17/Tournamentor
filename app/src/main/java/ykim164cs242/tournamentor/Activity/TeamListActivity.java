package ykim164cs242.tournamentor.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import ykim164cs242.tournamentor.Adapter.TeamListAdapter;
import ykim164cs242.tournamentor.R;
import ykim164cs242.tournamentor.ListItem.TeamListItem;

public class TeamListActivity extends AppCompatActivity {

    ListView teamListView;

    private TeamListAdapter adapter;
    private List<TeamListItem> teamListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

     //   teamListView = (ListView) findViewById(R.id.match_list);
//
     //   teamListItems = new ArrayList<>();
//
     //   adapter = new TeamListAdapter(this, teamListItems);
     //   teamListView.setAdapter(adapter);
//
     //   for(int i = 0; i < 3; i++) {
     //       teamListItems.add(new TeamListItem(i, "AS Dreuoit", "2013", "C. Ronaldo"));
     //   }

    }
}
