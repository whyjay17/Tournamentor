package ykim164cs242.tournamentor.Activity.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ykim164cs242.tournamentor.Activity.Client.ClientTournamentListActivity;
import ykim164cs242.tournamentor.Activity.Client.SelectChannelActivity;
import ykim164cs242.tournamentor.InformationStorage.TournamentInfo;
import ykim164cs242.tournamentor.R;

/**
 * TeamListActivity represents a screen of displaying participating teams of the tournament.
 * The team information is fetched from the Firebase real-time database and displayed
 * in the ListView of the teams.
 */
public class AdminAddTournamentActivity extends AppCompatActivity {


    private EditText inputName;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    private Button nextButton;
    private String channelID;

    private String startDate;
    private String endDate;

    // Firebase Database references.
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_tournament);
        nextButton = (Button) findViewById(R.id.next_button);
        inputName = (EditText) findViewById(R.id.add_tournament_name);
        // passed in data

        // Receiving data from the SelectChannel Activity
        try {
            Intent intent = getIntent();
            channelID = intent.getStringExtra("channelID");
        } catch(Exception e) {
            e.printStackTrace();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        startDateTextView = (TextView) findViewById(R.id.start_date);
        endDateTextView = (TextView) findViewById(R.id.end_date);

        startDateTextView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AdminAddTournamentActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, startDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        endDateTextView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AdminAddTournamentActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, endDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                startDateTextView.setText(date);
                startDate = date;
            }
        };

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                endDateTextView.setText(date);
                endDate = date;
            }
        };

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add tournament dialog

                Intent addTeamIntent = new Intent(AdminAddTournamentActivity.this, AdminAddTournamentAddTeamActivity.class);

                String nametoPass = inputName.getText().toString();
                String datetoPass = startDate + " ~ " + endDate;
                addTeamIntent.putExtra("inputName", nametoPass);
                addTeamIntent.putExtra("inputDate", datetoPass);
                TournamentInfo tempTournament = new TournamentInfo(nametoPass, datetoPass, null, null);
                rootReference.child("Channels").child(firebaseUser.getUid()).child("tournaments").child(nametoPass).setValue(tempTournament);
                startActivity(addTeamIntent);

            }
        });

    }
}
