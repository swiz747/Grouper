package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tritiumlabs.grouper.MainActivity;
import com.tritiumlabs.grouper.MyXMPP;
import com.tritiumlabs.grouper.R;
import fragments.SignupFragment;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final int REQUEST_SIGNUP = 0;
    public static MyXMPP xmppConnection;

    EditText userName;
    EditText passwordText;
    ImageButton btnLogin;
    TextView signupLink;
    CheckBox rememberPassword;
    CheckBox stayLoggedIn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout, container, false);

        userName = (EditText)view.findViewById(R.id.edittxtUsername);
        passwordText = (EditText)view.findViewById(R.id.edittxtPassword);
        btnLogin = (ImageButton)view.findViewById(R.id.imgLogin);
        signupLink = (TextView)view.findViewById(R.id.txtRegisterNow);
        rememberPassword = (CheckBox)view.findViewById(R.id.rememberPasswordBox);
        stayLoggedIn = (CheckBox)view.findViewById(R.id.stayLoggedBox);

        ImageView fishImage = (ImageView) view.findViewById(R.id.imgFishLogoBlink);
        fishImage.setBackgroundResource(R.drawable.fish_blink_animation);
        AnimationDrawable fishBlinkAnimation = (AnimationDrawable) fishImage.getBackground();
        fishBlinkAnimation.start();

        if (getArguments() != null) {
            Bundle userInfo = getArguments();
            if (userInfo.getString("name") != null) {
                EditText someshit = (EditText) view.findViewById(R.id.edittxtUsername);
                someshit.setText(userInfo.getString("name"));
            }
        }
        else
        {
            loadInfo();
        }

        xmppConnection = MyXMPP.getInstance(getActivity());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupScreen(null);
            }
        });

        return view;
    }

    public void openSignupScreen(@Nullable Bundle argsBundle) {

        Fragment toFragment = new SignupFragment();
        if (argsBundle != null)
        {
            toFragment.setArguments(argsBundle);
        }
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.zoom_in, R.animator.zoom_out, R.animator.zoom_back_out, R.animator.zoom_back_in)
                .replace(R.id.fragContainer, toFragment)
                .addToBackStack(null)
                .commit();
    }

    public void openMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("validation fail");
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        final String user = userName.getText().toString();
        final String password = passwordText.getText().toString();
        xmppConnection.setLoginCreds(user, password);

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
                    onLoginSuccess(user,password);
                }
                else
                {
                    onLoginFailed("");
                }
            }
        };
        connectionThread.execute();
    }

    public void onLoginSuccess(String user, String password) {
        btnLogin.setEnabled(true);
        //TODO add username and password to DB -AB
        MyXMPP.dbHandler.setUserName(user);
        Log.d("derp", user);
        MyXMPP.dbHandler.setUserPassword(password);
        saveInfo();
        openMainActivity();
        getActivity().finish();
    }

    public void onLoginFailed(String why) {
        Toast.makeText(getActivity().getBaseContext(), "Login failed" + why, Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = userName.getText().toString();

        if (email.isEmpty()) {
            userName.setError("Enter a valid username.");
            valid = false;
        } else {
            userName.setError(null);
        }

        return valid;
    }

    public void saveInfo() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean("loginStatus", true);
        editor.putString("username", userName.getText().toString());
        editor.putString("hiddenPass", passwordText.getText().toString());

        if (!sharedPref.contains("citystate")) {
            editor.putString("citystate", "");
        }

        if (!sharedPref.contains("userbio")) {
            editor.putString("userbio", "");
        }

        if (!sharedPref.contains("userinfo")) {
            editor.putString("userinfo", "");
        }

        if (rememberPassword.isChecked()) {
            editor.putString("password", passwordText.getText().toString());
            editor.putBoolean("rememberPass", true);
        } else {
            editor.putString("password", null);
            editor.putBoolean("rememberPass", false);
        }

        if (stayLoggedIn.isChecked()) {
            editor.putBoolean("stayLoggedIn", true);
        } else {
            editor.putBoolean("stayLoggedIn", false);
        }

        editor.apply();
    }

    public void loadInfo() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String name = sharedPref.getString("username", "");
        String pass = sharedPref.getString("password", "");
        userName.setText(name);
        passwordText.setText(pass);

        if (sharedPref.getBoolean("rememberPass", false)) {
            rememberPassword.setChecked(true);
        } else {
            rememberPassword.setChecked(false);
        }

        if (sharedPref.getBoolean("stayLoggedIn", false)) {
            stayLoggedIn.setChecked(true);
        } else {
            stayLoggedIn.setChecked(false);
        }
    }
}
