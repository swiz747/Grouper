package com.tritiumlabs.grouper;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import fragments.settingsfragments.AccountFragment;

public class SettingsActivity extends AppCompatActivity {

    Button btnProfile;
    Button btnAccount;
    Button btnPrivacy;
    Button btnThemes;
    Button btnNotifications;

    AnimationDrawable fishBlinkAnimation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //Objects
        btnAccount = (Button)findViewById(R.id.btnAccount);


        ImageView fishImage = (ImageView) findViewById(R.id.imgFishBlink);
        fishImage.setBackgroundResource(R.drawable.fish_blink_animation);
        fishBlinkAnimation = (AnimationDrawable) fishImage.getBackground();
        fishBlinkAnimation.start();

        //Listeners
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccountFragment();
            }
        });
    }

    public void openAccountFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragContainerSettings, new AccountFragment()).addToBackStack("settingsAccountFragment").commit();
    }
}
