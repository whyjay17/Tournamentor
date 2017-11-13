package ykim164cs242.tournamentor.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ykim164cs242.tournamentor.R;

public class MainActivity extends AppCompatActivity {

    // Time

    private static int SPLASH_TIME = 3000; // Milliseconds, 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent nextIntent = new Intent(MainActivity.this, StartMenuActivity.class);
                startActivity(nextIntent);
                finish();
            }
        }, SPLASH_TIME);
    }
}
