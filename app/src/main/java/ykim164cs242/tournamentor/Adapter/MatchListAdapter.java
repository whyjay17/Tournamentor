package ykim164cs242.tournamentor.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ykim164cs242.tournamentor.Activity.UserMainActivity;
import ykim164cs242.tournamentor.ListItem.MatchListItem;
import ykim164cs242.tournamentor.R;

/**
 * MatchListAdapter represents an Adapter that can be used in the ListView of matches.
 * It puts in the right data inside the activity_match_list_items.
 */

public class MatchListAdapter extends BaseAdapter{

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tournamentReference = rootReference.child("Tournaments");

    private Context context;
    private List<MatchListItem> matchList;

    public MatchListAdapter(Context context, List<MatchListItem> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchList.get(position);
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
        TextView liveStatus = (TextView) view.findViewById(R.id.live_status);

        // Favorite Star

        final ImageView starred = (ImageView) view.findViewById(R.id.starred);

        // Set Texts for TextViews

        fieldName.setText(matchList.get(position).getFieldName());
        gameTime.setText(matchList.get(position).getGameTime());
        gameDate.setText(matchList.get(position).getGameDate());
        teamA.setText(matchList.get(position).getTeamA());
        teamB.setText(matchList.get(position).getTeamB());
        scoreA.setText(Integer.toString(matchList.get(position).getScoreA()));
        scoreB.setText(Integer.toString(matchList.get(position).getScoreB()));

        final DatabaseReference matchReference = tournamentReference.child("Test Tournament").child("Matches").child(matchList.get(position).getGameDate() + " "
                + matchList.get(position).getTeamA() + " vs "
        + matchList.get(position).getTeamB()).child("isStarred");

        // Live Status : Blink effect if game is live

        if(matchList.get(position).isLive()) {
            // Blink Text If Live
        } else {
            liveStatus.setText("");
        }

        // Starred Status: Turns star into Yellow if the game is starred

        if(matchList.get(position).isStarred()) {
            starred.setColorFilter(Color.argb(255, 255, 214, 51));
        } else { }

        starred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(matchList.get(position).isStarred()) {

                    // when the Colored star is clicked, change color back to white and set the "Starred" value to false in both data and real-time database

                    matchList.get(position).setStarred(false);
                    matchReference.setValue(false);
                    starred.setColorFilter(Color.argb(255, 255, 255, 255)); // White
                }
                else {

                    // when the unColored star is clicked, change color to yellow and set the "Starred" value to true in both data and real-time database

                    matchList.get(position).setStarred(true);
                    matchReference.setValue(true);
                    starred.setColorFilter(Color.argb(255, 255, 214, 51)); // Yellow
                }
            }
        });

        return view;
    }
}
