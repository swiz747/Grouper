package com.tritiumlabs.grouper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_progress_bar);

        Intent intent = new Intent(this, StarterActivity.class);
        startActivity(intent);
        finish();
    }
}