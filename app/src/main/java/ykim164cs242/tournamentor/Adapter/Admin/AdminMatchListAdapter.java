package ykim164cs242.tournamentor.Adapter.Admin;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ykim164cs242.tournamentor.ListItem.AdminMatchListItem;
import ykim164cs242.tournamentor.ListItem.MatchListItem;
import ykim164cs242.tournamentor.R;

/**
 * MatchListAdapter represents an Adapter that can be used in the ListView of matches.
 * It puts in the right data inside the activity_match_list_items.
 */

public class AdminMatchListAdapter extends BaseAdapter{

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tournamentReference = rootReference.child("Tournaments");

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

        final View view = View.inflate(context, R.layout.activity_match_list_item, null);
        TextView fieldName = (TextView) view.findViewById(R.id.comp_name);
        TextView gameTime = (TextView) view.findViewById(R.id.game_time);
        TextView gameDate = (TextView) view.findViewById(R.id.game_date);
        TextView teamA = (TextView) view.findViewById(R.id.team_a);
        TextView teamB = (TextView) view.findViewById(R.id.team_b);
        TextView scoreA = (TextView) view.findViewById(R.id.score_a);
        TextView scoreB = (TextView) view.findViewById(R.id.score_b);
        Switch liveSwitch = (Switch) view.findViewById(R.id.live_status_switch);

        // Manage buttons

        Button updateScoreButton = (Button) view.findViewById(R.id.update_score_button);
        Button endGameButton = (Button) view.findViewById(R.id.end_game_button);

        updateScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO: update score dialog
            }
        });

        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: End game
                adminMatchList.get(position).setLive(false);
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

        // Live Status : Blink effect if game is live

        liveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // If game live is checked
                    Toast.makeText(context, "Is live", Toast.LENGTH_SHORT).show();
                    adminMatchList.get(position).setLive(true);
                } else {
                    // Pause game
                    Toast.makeText(context, "Is paused", Toast.LENGTH_SHORT).show();
                    adminMatchList.get(position).setLive(false);
                }
            }
        });

        return view;
    }
}
