package fragments.settingsfragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;

import com.tritiumlabs.grouper.MyXMPP;
import com.tritiumlabs.grouper.R;

public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";

    AccountManager accountManager;

    Button btnChangeEmail;
    Button btnChangePassword;

    private String inputText = "";
    private String confirmText = "";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_account_layout, container, false);

        //UI objects
        btnChangeEmail = (Button)view.findViewById(R.id.btnChangeEmail);
        btnChangePassword = (Button)view.findViewById(R.id.btnChangePassword);

        //Listeners
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputBox("Enter your new Password:", "passchange");
            }
        });

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputBox("Enter your new email:", "emailchange");
            }
        });

        return view;
    }

    public void changeUsername() {

    }

    public void changeEmail() {

    }

    public void changePassword() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);

        accountManager = AccountManager.getInstance(MyXMPP.connection);
        accountManager.sensitiveOperationOverInsecureConnection(true);

        AsyncTask<Void, Void, Boolean> signupThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute()
            {

                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Account...");
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                accountManager = AccountManager.getInstance(MyXMPP.connection);
                accountManager.sensitiveOperationOverInsecureConnection(true);

                try {
                    accountManager.changePassword(inputText);
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
                    Log.d(TAG, inputText);
                    onPasswordChangeSuccess();
                }
                else
                {
                    //onPasswordChangeFail();
                }
            }
        };
        signupThread.execute();
    }

    public void inputBox(String title, String reason) {

        final String boxReason = reason;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputText = input.getText().toString();
                if (boxReason.equals("emailchange")) {
                    changeEmail();
                } else if (boxReason.equals("passchange")) {
                    if (inputText.isEmpty() || inputText.length() < 6 || inputText.length() > 24) {
                        passwordErrorBox("Password must be between 6 and 24 alphanumeric characters.");
                    } else {
                        confirmBox("Confirm Password:", "passconfirm");
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void confirmBox(String title, String reason) {

        final String boxReason = reason;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmText = input.getText().toString();
                if (boxReason.equals("passconfirm")) {
                    if (confirmText.equals(inputText)) {
                        changePassword();
                    } else {
                        passwordErrorBox("Passwords to not match.");
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void passwordErrorBox(String reason) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(reason);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        inputBox("Enter your new Password", "passchange");
                    }
                });

        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    public void onPasswordChangeSuccess() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("password", inputText);
        editor.apply();
        Toast.makeText(getActivity().getBaseContext(), "Password Changed", Toast.LENGTH_LONG).show();
    }
}
