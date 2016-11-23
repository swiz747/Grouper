package com.tritiumlabs.grouper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.ListView;

import com.tritiumlabs.grouper.R;
import com.tritiumlabs.grouper.adapters.MainDrawerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fragments.FriendAddFragment;
import fragments.FriendsListFragment;
import fragments.LoginFragment;
import fragments.groupiefragments.CreateGroupieFragment;
import fragments.groupiefragments.DeleteGroupieFragment;
import fragments.groupiefragments.EditGroupieFragment;
import fragments.mainfragments.FriendsFragment;
import fragments.mainfragments.GroupChatFragment;
import fragments.mainfragments.GroupiesFragment;
import fragments.mainfragments.HomeFragment;
import fragments.mainfragments.InboxFragment;
import fragments.mainfragments.ProfileFragment;
import fragments.mainfragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean mBounded = false;
    public boolean isActive;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    MainDrawerAdapter adapter;
    List<MainDrawerItem> drawerDataList;

    private MyService mService;

    private static int lastClicked = 0;

    //TODO Check shared preferences, see if already logged in.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActive = true;
        Log.d(TAG, "on create method");

        Intent i = new Intent(this, MyService.class);
        bindService(i, mConnection, Context.BIND_ABOVE_CLIENT);
        startService(i);
        setContentView(R.layout.main_activity_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openHomeFragmentInitial();

        drawerDataList = new ArrayList<MainDrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerDataList.add(new MainDrawerItem("Home", R.drawable.home_button));
        drawerDataList.add(new MainDrawerItem("Inbox", R.drawable.inbox_button));
        drawerDataList.add(new MainDrawerItem("FriendS", R.drawable.friends_button));
        drawerDataList.add(new MainDrawerItem("Group Chat", R.drawable.group_chat_button));
        drawerDataList.add(new MainDrawerItem("Profile", R.drawable.profile_button));
        drawerDataList.add(new MainDrawerItem("Groupies", R.drawable.groupie_button));
        drawerDataList.add(new MainDrawerItem("Settings", R.drawable.settings_button));
        drawerDataList.add(new MainDrawerItem("Logout", R.drawable.logout_button));

        adapter = new MainDrawerAdapter(this, R.layout.custom_drawer_item, drawerDataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public MyService getmService() {
        return mService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalDBHandler handler = LocalDBHandler.getInstance(this);
        unbindService(mConnection);
        handler.close();
        isActive = false;
        MyXMPP.disconnectWithPresence();
    }

    @Override
    public void onBackPressed() {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (homeFragment != null && homeFragment.isVisible()) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // LocalDBHandler handler = LocalDBHandler.getInstance(this);
            //handler.getLocalSettings();
            //TODO send to settings fragment.
            return true;
        } else if (id == R.id.fuck_everything) {
            //for when you need to fuck everything -AB
            //LocalDBHandler handler = LocalDBHandler.getInstance(this);
            //handler.fuckeverything();
            return true;
        } else if (id == R.id.add_friend) {
            //TODO temporary add friend
            openFriendAdd();
            return true;
        } else if (id == R.id.mystery) {
            openMapFragment();
            return true;
        } else if (id == R.id.refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        InboxFragment inboxFragment = (InboxFragment) getSupportFragmentManager().findFragmentByTag("InboxFragment");
        FriendsFragment friendsFragment = (FriendsFragment) getSupportFragmentManager().findFragmentByTag("FriendsFragment");
        GroupChatFragment groupChatFragment = (GroupChatFragment) getSupportFragmentManager().findFragmentByTag("GroupChatFragment");
        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("ProfileFragment");
        GroupiesFragment groupiesFragment = (GroupiesFragment) getSupportFragmentManager().findFragmentByTag("GroupiesFragment");

        if (position == 0) {
            if (homeFragment != null && homeFragment.isVisible()) {

            } else {
                openHomeFragment(position);
            }
        } else if (position == 1) {
            if (inboxFragment != null && inboxFragment.isVisible()) {

            } else {
                openInboxFragment(position);
            }
        } else if (position == 2) {
            if (friendsFragment != null && friendsFragment.isVisible()) {

            } else {
                openFriendsFragment(position);
            }
        } else if (position == 3) {
            if (groupChatFragment != null && groupChatFragment.isVisible()) {

            } else {
                openGroupChatFragment(position);
            }
        } else if (position == 4) {
            if (profileFragment != null && profileFragment.isVisible()) {

            } else {
                openProfileFragment(position);
            }
        } else if (position == 5) {
            if (groupiesFragment != null && groupiesFragment.isVisible()) {

            } else {
                openGroupiesFragment(position);
            }
        } else if (position == 6) {
            openSettings();
        } else if (position == 7) {
            openLogin();
        }
    }

    private void openGroupChatFragment(int position) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        GroupChatFragment fragment = new GroupChatFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerMain, fragment, "GroupChatFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(drawerDataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void openProfileFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        ProfileFragment fragment = new ProfileFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerMain, fragment, "ProfileFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(drawerDataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void openGroupiesFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        GroupiesFragment fragment = new GroupiesFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerMain, fragment, "GroupiesFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(drawerDataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void openFriendsFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        FriendsFragment fragment = new FriendsFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerMain, fragment, "FriendsFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(drawerDataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void openInboxFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        InboxFragment fragment = new InboxFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerMain, fragment, "InboxFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(drawerDataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void openHomeFragment(int position) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        HomeFragment homeFragment = new HomeFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerMain, homeFragment, "HomeFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(drawerDataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void openHomeFragmentInitial() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        HomeFragment homeFragment = new HomeFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerMain, homeFragment, "HomeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openFriendAdd() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragContainer, new FriendAddFragment()).addToBackStack("addFriend").commit();
    }

    public void openFriendsList() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragContainer, new FriendsListFragment()).addToBackStack("friendslist").commit();
    }

    public void openLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Logging Out...");
                progressDialog.show();
            }
            @Override
            protected Boolean doInBackground(Void... params)
            {
                MyXMPP.disconnectWithPresence();
                return true;
            }
            @Override
            protected void onPostExecute(Boolean result)
            {
                progressDialog.dismiss();

            }
        };
        connectionThread.execute();

        updatePreferences();
        Intent intent = new Intent(this, StarterActivity.class);
        startActivity(intent);
        finish();
    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openMapFragment() {

    }

    public void updatePreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("loginStatus", false);
        editor.apply();
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
            mBounded = true;
            Log.d(TAG, "Connected the service");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };
}
