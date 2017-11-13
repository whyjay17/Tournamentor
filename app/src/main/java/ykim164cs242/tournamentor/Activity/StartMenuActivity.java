package ykim164cs242.tournamentor.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ykim164cs242.tournamentor.R;

public class StartMenuActivity extends AppCompatActivity {

    Button joinChannelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        joinChannelButton = (Button) findViewById(R.id.join_channel_button);

        joinChannelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent channelIntent = new Intent(StartMenuActivity.this, SelectChannelActivity.class);
                startActivity(channelIntent);

            }
        });
    }
}
