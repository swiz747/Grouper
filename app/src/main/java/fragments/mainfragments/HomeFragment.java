package fragments.mainfragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.vision.text.Text;
import com.tritiumlabs.grouper.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import interfaces.ExternalDB;
import objects.ExternalDBResponse;
import objects.Groupie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment{

    private ScrollView scrMainScroll;
    private ScrollView scrSubScroll;

    private TextView txtWelcome;
    private TextView txtUsername;
    private TextView txtCityState;
    private TextView txtUserbio;
    private TextView txtUserAge;
    private TextView txtUserBirthday;
    private TextView txtUserGender;
    private TextView txtRecentActivity;
    private ImageView imgProfilePicture;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_home_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        scrMainScroll = (ScrollView) view.findViewById(R.id.scrMainHomeFragmentMainScrollView);
        scrSubScroll = (ScrollView) view.findViewById(R.id.scrMainHomeFragmentSubScrollView);
        txtWelcome = (TextView) view.findViewById(R.id.txtMainHomeFragmentTextHeader);
        txtUsername = (TextView) view.findViewById(R.id.txtMainHomeFragmentUsername);
        txtCityState = (TextView) view.findViewById(R.id.txtMainHomeFragmentUserState);
        txtUserbio = (TextView) view.findViewById(R.id.txtMainHomeFragmentUserBio);
        txtUserAge = (TextView) view.findViewById(R.id.txtMainHomeFragmentUserAge);
        txtUserBirthday = (TextView) view.findViewById(R.id.txtMainHomeFragmentUserBirthday);
        txtUserGender = (TextView) view.findViewById(R.id.txtMainHomeFragmentUserGender);
        txtRecentActivity = (TextView) view.findViewById(R.id.txtHomeRecentActivity);

        txtWelcome.setTypeface(sab);
        txtUsername.setTypeface(sab);
        txtCityState.setTypeface(sab);
        txtUserbio.setTypeface(sab);
        txtRecentActivity.setTypeface(sab);

        //TODO: Remove comments once real font is purchased -KD
        //txtUserAge.setTypeface(sab);
        //txtUserBirthday.serTypeface(sab);
        //txtUserGender.setTypeface(sab);

        //TODO: Should be pulling this info from the database rather than a shared preference. -KD
        String username = sharedPref.getString("username", "");
        String state = sharedPref.getString("state", "");
        String userbio = sharedPref.getString("userbio", "");
        int userAge = sharedPref.getInt("userage", 0);
        String userBirthday = sharedPref.getString("userbirthday", "");
        int userMalePercent = sharedPref.getInt("malepercent", 0);
        int userFemalePercent = sharedPref.getInt("femalepercent", 0);

        txtUsername.setText(username);

        if (state.equals("")) {
            txtCityState.setText("Enter State");
            txtCityState.setTextColor(getResources().getColor(R.color.primary_darkest_translucent));
        } else {
            txtCityState.setText(state);
        }

        if (userbio.equals("")) {
            txtUserbio.setText("Enter bio");
            txtUserbio.setTextColor(getResources().getColor(R.color.primary_darkest_translucent));
        } else {
            txtUserbio.setText(userbio);
        }

        if (userAge == 0 || userBirthday.equals("")) {
            txtUserAge.setText("Tap to enter Birthday");
            txtUserBirthday.setVisibility(View.GONE);
        } else {
            txtUserAge.setText("Age: " + userAge);
            txtUserBirthday.setText(userBirthday);
        }

        if (userMalePercent == 0 && userFemalePercent == 0) {
            txtUserGender.setText("Tap to enter gender");
        } else {
            txtUserGender.setText(userMalePercent + "% Male\n" + userFemalePercent + "% Female");
        }

        try {
            ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgProfilePicture = (ImageView) view.findViewById(R.id.imgMainHomeFragmentProfileImage);
            imgProfilePicture.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        final TextView areaCount = (TextView) view.findViewById(R.id.txtAreaCount);

        ExternalDB dbInterface = ExternalDB.retrofit.create(ExternalDB.class);
        final Call<List<ExternalDBResponse>> call = dbInterface.getAreaCount("usernameHere",5.0,5,2);

        call.enqueue(new Callback<List<ExternalDBResponse>>() {
            @Override
            public void onResponse(Call<List<ExternalDBResponse>> call, Response<List<ExternalDBResponse>> response)
            {

                Log.d("response: ", response.body().get(0).getMainResponse());
                Log.d("response: ", response.body().get(0).getResponseCode());
                Log.d("response: ", response.body().get(0).getResponseMessage());
                Log.d("response: ", response.body().get(0).getEchoInput());

                //TODO set this as a string resource and whatnot -AB

                areaCount.setText(response.body().get(0).getMainResponse() + " Users in your area!");


            }

            @Override
            public void onFailure(Call<List<ExternalDBResponse>> call, Throwable t)
            {
                Log.d("Tracker", t.getMessage());
            }
        });

        dildoTest();

        return view;
    }

    private void dildoTest()
    {
        Groupie derp = new Groupie();

        derp.setGroupieName("groupiename");
        derp.setGroupieCreator("testUser");
        derp.setGroupieDescription("buttplug party, cum get sum fuk");
        derp.setPrivateIndicator(false);
        derp.setGroupieStartDate("12/01/2016");
        derp.setGroupieStartTime("16:30");
        derp.setGroupieEndDate("12/01/2016");
        derp.setGroupieEndTime("20:00");
        derp.setGroupieLat(41.696969);
        derp.setGroupieLong(-71.424242);
        derp.setGroupieAddress(null);

        Log.d("DERP", derp.groupieCreationString());


    }


}
