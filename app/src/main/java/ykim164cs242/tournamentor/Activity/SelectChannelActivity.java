package ykim164cs242.tournamentor.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ykim164cs242.tournamentor.Adapter.ChannelListAdapter;
import ykim164cs242.tournamentor.Adapter.MatchListAdapter;
import ykim164cs242.tournamentor.ListItem.ChannelListItem;
import ykim164cs242.tournamentor.ListItem.MatchListItem;
import ykim164cs242.tournamentor.R;

/**
 * SelectChannelActivity represents a screen of selecting existing tournament channels.
 * The channel information is fetched from the Firebase real-time database and
 * displayed in the ListView of the channel.
 */

public class SelectChannelActivity extends AppCompatActivity {

    ListView channelListView;

    private ChannelListAdapter adapter;
    private List<ChannelListItem> channelListItems;

    // Storages for parsed data from the real-time database
    private List<String> channelNameList;
    private List<String> termList;
    private List<String> hostList;

    // Realtime database reference

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference channelReference = rootReference.child("Channels");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_channel);

        channelListView = (ListView) findViewById(R.id.channel_listview);
        channelListItems = new ArrayList<>();
        channelNameList = new ArrayList<>();
        termList = new ArrayList<>();
        hostList = new ArrayList<>();

        adapter = new ChannelListAdapter(this, channelListItems);
        channelListView.setAdapter(adapter);

        // onClickHandler for each list item. It moves to the corresponding channel passing the tournamentName data to the next activity

        channelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SelectChannelActivity.this, UserMainActivity.class);
                String key = channelNameList.get(position);
                intent.putExtra("tournamentName", key);

                startActivity(intent);

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

        channelReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Clear the storages for redrawing of the ListView
                channelNameList.clear();
                termList.clear();
                hostList.clear();

                // Fires every single time the channelReference updates in the Real-time DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    channelListItems.clear();
                    channelNameList.add(snapshot.child("Name").getValue().toString());
                    termList.add(snapshot.child("Term").getValue().toString());
                    hostList.add(snapshot.child("Host").getValue().toString());
                }

                for(int i = 0; i < channelNameList.size(); i++) {
                    channelListItems.add(new ChannelListItem(i, channelNameList.get(i), termList.get(i), hostList.get(i)));
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
