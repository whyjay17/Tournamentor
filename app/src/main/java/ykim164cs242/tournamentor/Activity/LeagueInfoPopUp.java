package ykim164cs242.tournamentor.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import ykim164cs242.tournamentor.R;

/**
 * Created by YJ on 11/12/2017.
 */

class LeagueInfoPopUp extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_league_info_popup);


        // Display Metrics object that contains Width & Height of the screen

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.5));

    }
}
