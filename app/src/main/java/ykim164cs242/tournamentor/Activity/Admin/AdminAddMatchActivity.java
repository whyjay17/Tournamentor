package ykim164cs242.tournamentor.Activity.Admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ykim164cs242.tournamentor.InformationStorage.GameInfo;
import ykim164cs242.tournamentor.InformationStorage.TournamentInfo;
import ykim164cs242.tournamentor.R;

/**
 * AdminAddMatchActivity represents a screen where the Admin can add a new
 * game in the tournament. Based on the final setting, it updates
 * the Firebase database.
 */
public class AdminAddMatchActivity extends AppCompatActivity {

    private EditText inputFieldName;
    private TextView inputMatchDate;
    private TextView inputMatchTime;
    private EditText inputTeamA;
    private EditText inputTeamB;
    private Button submitButton;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private String tournamentName;
    private String fieldName;
    private String gameDate;
    private String gameTime;
    private String teamA;
    private String teamB;

    // Firebase Database references.
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_match);

        // Gets the tournamentName from the previous Activity
        try {
            Intent intent = getIntent();

            tournamentName = intent.getStringExtra("tournamentName");

        } catch(Exception e) {
            e.printStackTrace();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        inputFieldName = (EditText) findViewById(R.id.input_field_name);
        inputMatchDate = (TextView) findViewById(R.id.input_match_date);
        inputMatchTime = (TextView) findViewById(R.id.input_match_time);
        inputTeamA = (EditText) findViewById(R.id.input_team_A);
        inputTeamB = (EditText) findViewById(R.id.input_team_B);
        submitButton = (Button) findViewById(R.id.add_game_button);

        // Date selector dialog

        inputMatchDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AdminAddMatchActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // Time selector dialog

        inputMatchTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AdminAddMatchActivity.this, timeSetListener, hour, min, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();


            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = month + "-" + dayOfMonth + "-" + year;
                inputMatchDate.setText(date);

                gameDate = date;

            }
        };

        timeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String time = hourOfDay + " : " + minute;
                inputMatchTime.setText(time);
                gameTime = time;

            }
        };


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fieldName = inputFieldName.getText().toString();
                teamA = inputTeamA.getText().toString();
                teamB = inputTeamB.getText().toString();

                // Game ID Format is date + teamA + teamB
                String gameID = gameDate + teamA + teamB;

                // Adds the gameInfo to the Databse
                GameInfo gameInfo = new GameInfo(gameID, fieldName, gameTime, gameDate, teamA, teamB, "0", "0", false, tournamentName);
                rootReference.child("Channels").child(firebaseUser.getUid()).child("tournaments").child(tournamentName).child("games").child(gameID).setValue(gameInfo);

                Intent intent = new Intent(AdminAddMatchActivity.this, AdminMatchListActivity.class);

                // Passes in the tournamentName
                intent.putExtra("tournamentName", tournamentName);
                startActivity(intent);

            }
        });
    }
}
