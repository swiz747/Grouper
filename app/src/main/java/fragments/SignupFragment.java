package fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tritiumlabs.grouper.MyXMPP;
import com.tritiumlabs.grouper.R;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.util.HashMap;
import java.util.Map;

public class SignupFragment extends Fragment {

    private static final String TAG = "SignupActivity";
    private static final int REQUEST_LOGIN = 0;
    AccountManager accountManager;
    EditText nameText;
    EditText userEmail;
    EditText passwordText;
    EditText passwordConfirmText;
    ImageButton btnSignup;
    TextView loginLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_layout, container, false);

        nameText = (EditText)view.findViewById(R.id.edittxtUsername);
        userEmail = (EditText)view.findViewById(R.id.edittxtEmail);
        passwordText = (EditText)view.findViewById(R.id.edittxtPassword);
        passwordConfirmText = (EditText)view.findViewById(R.id.edittxtPasswordConfirm);
        btnSignup = (ImageButton)view.findViewById(R.id.imgbtnRegister);
        loginLink = (TextView)view.findViewById(R.id.txtLogin);

        //TODO: this is for easy testing because im lazy -AB
        nameText.setText("tester");
        userEmail.setText("Dildo@gmail.com");
        passwordText.setText("fuck123");

        btnSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openLoginScreen(null);
            }
        });

        return view;
    }

    public void openLoginScreen(@Nullable Bundle argsBundle) {
        Fragment toFragment = new LoginFragment();
        if (argsBundle != null)
        {
            toFragment.setArguments(argsBundle);
        }

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.zoom_back_out, R.animator.zoom_back_in, R.animator.zoom_in, R.animator.zoom_out)
                .replace(R.id.fragContainer, toFragment)
                .addToBackStack(null)
                .commit();
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignup.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        final String name = nameText.getText().toString();
        final String email = userEmail.getText().toString();
        final String password = passwordText.getText().toString();
        final Map<String,String> attributes = new HashMap<>();
        attributes.put("email", email);

        AsyncTask<Void, Void, Boolean> signupThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute()
            {
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                MyXMPP.connect("Signup");
                accountManager = AccountManager.getInstance(MyXMPP.connection);
                accountManager.sensitiveOperationOverInsecureConnection(true);

                try {
                    accountManager.createAccount(name, password, attributes);
                } catch (XMPPException e1) {
                    Log.d(e1.getMessage(), String.valueOf(e1));
                    return false;
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                    return false;
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                progressDialog.dismiss();
                if (result)
                {
                    onSignupSuccess(name);
                }
                else
                {

                }
            }
        };
        signupThread.execute();
    }

    public void onSignupSuccess(String name) {
        MyXMPP.disconnect();
        btnSignup.setEnabled(true);
        Bundle args = new Bundle();
        args.putString("name", nameText.getText().toString());
        openLoginScreen(args);
    }

    public void onSignupFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = userEmail.getText().toString();
        String password = passwordText.getText().toString();
        String passwordConfirm = passwordConfirmText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("Name must be at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            userEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 24) {
            passwordText.setError("Password must be between 6 and 24 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (passwordConfirm.isEmpty() || passwordConfirm.equals(password) != true) {
            passwordConfirmText.setError("Passwords do not match");
            valid = false;
        } else {
            passwordConfirmText.setError(null);
        }

        return valid;
    }
}