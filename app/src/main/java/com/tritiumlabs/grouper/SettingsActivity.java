package com.tritiumlabs.grouper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fragments.settingsfragments.AccountFragment;
import fragments.settingsfragments.DefaultFragment;

public class SettingsActivity extends AppCompatActivity {

    Button btnProfile;
    Button btnAccount;
    Button btnPrivacy;
    Button btnThemes;
    Button btnNotifications;

    FragmentManager manager;

    public String currentFrag;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        openDefaultFragment();

        //Objects
        btnAccount = (Button)findViewById(R.id.btnAccount);

        //Listeners
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccountFragment();
            }
        });
    }

    public void openDefaultFragment() {
        currentFrag = "default";
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerSettings, new DefaultFragment()).addToBackStack("settingsDefaultFragment").commit();
    }

    public void openAccountFragment() {
        currentFrag = "account";
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerSettings, new AccountFragment()).addToBackStack("settingsAccountFragment").commit();
    }
}
