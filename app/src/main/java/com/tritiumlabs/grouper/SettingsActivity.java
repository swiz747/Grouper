package com.tritiumlabs.grouper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    Button btnProfile;
    Button btnPrivacy;
    Button btnThemes;
    Button btnNotifications;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


    }
}
