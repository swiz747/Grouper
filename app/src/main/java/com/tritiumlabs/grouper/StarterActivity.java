package com.tritiumlabs.grouper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.XMPPConnection;

import fragments.LoginFragment;

public class StarterActivity extends AppCompatActivity {
    private static final String TAG = "StarterActivity";
    private boolean mBounded = false;
    public boolean isActive;
    public static MyXMPP xmppConnection;

    //TODO Check shared preferences, see if already logged in.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        isActive = true;
        setContentView(R.layout.starter_activity);

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        boolean stayLogged = sharedPref.getBoolean("stayLoggedIn", false);
        if (stayLogged)
        {
            xmppConnection = MyXMPP.getInstance(this);
            login();
        } else {
            openLoginScreen();
        }
    }

    public void openLoginScreen() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragContainer, new LoginFragment()).addToBackStack("login").commit();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void login() {
        Log.d(TAG, "Login");

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        final String name = sharedPref.getString("username", "");
        final String pass = sharedPref.getString("hiddenPass", "");
        xmppConnection.setLoginCreds(name, pass);

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
            }
            @Override
            protected Boolean doInBackground(Void... params)
            {
                xmppConnection.connect("login");
                return xmppConnection.getLoggedIn();
            }
            @Override
            protected void onPostExecute(Boolean result)
            {
                progressDialog.dismiss();
                if(result)
                {
                    loginSuccess(name, pass);
                } else {
                    loginFailed();
                }
            }
        };
        connectionThread.execute();
    }

    public void loginSuccess(String name, String password) {
        MyXMPP.dbHandler.setUserName(name);
        Log.d("derp", name);
        MyXMPP.dbHandler.setUserPassword(password);
        openMainActivity();
        finish();
    }

    public void loginFailed() {
        Toast.makeText(this.getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        openLoginScreen();
    }
}
