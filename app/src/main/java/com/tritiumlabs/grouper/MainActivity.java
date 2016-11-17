package com.tritiumlabs.grouper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;

import com.tritiumlabs.grouper.R;

import fragments.FriendsListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean mBounded = false;
    public boolean isActive;

    Button btnFriendsList;
    Button btnLogout;

    //TODO Check shared preferences, see if already logged in.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActive = true;
        setContentView(R.layout.main_activity);

        btnFriendsList = (Button)findViewById(R.id.btnFriendsList);
        btnLogout = (Button)findViewById(R.id.btnLogout);

        btnFriendsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsList();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePreferences();
                openLogin();
                finish();
            }
        });
    }

    public void openFriendsList() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragContainer, new FriendsListFragment()).addToBackStack("friendslist").commit();
    }

    public void openLogin() {
        MyXMPP.disconnect();
        Intent intent = new Intent(this, StarterActivity.class);
        startActivity(intent);
    }

    public void updatePreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("stayLoggedIn", false);
        editor.apply();
    }
}
