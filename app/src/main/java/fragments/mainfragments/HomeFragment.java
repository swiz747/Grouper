package fragments.mainfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.tritiumlabs.grouper.R;

import java.util.List;

import interfaces.ExternalDB;
import objects.ExternalDBResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment{

    private TextView txtWelcome;
    private TextView txtUsername;
    private TextView txtCityState;
    private TextView txtUserbio;
    private TextView txtUserinformation;
    private TextView txtRecentActivity;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_home_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        txtWelcome = (TextView) view.findViewById(R.id.txtHomeWelcomeBack);
        txtUsername = (TextView) view.findViewById(R.id.txtHomeUserName);
        txtCityState = (TextView) view.findViewById(R.id.txtHomeCityState);
        txtUserbio = (TextView) view.findViewById(R.id.txtHomeUserbio);
        txtUserinformation = (TextView) view.findViewById(R.id.txtHomeUserinfo);
        txtRecentActivity = (TextView) view.findViewById(R.id.txtHomeRecentActivity);

        txtWelcome.setTypeface(sab);
        txtUsername.setTypeface(sab);
        txtCityState.setTypeface(sab);
        txtUserbio.setTypeface(sab);
        txtRecentActivity.setTypeface(sab);
        //txtUserinformation.setTypeface(sab);

        //TODO: Should be pulling this info from the database rather than a shared preference. -KD
        String username = sharedPref.getString("username", "");
        String citystate = sharedPref.getString("citystate", "");
        String userbio = sharedPref.getString("userbio", "");
        String userinfo = sharedPref.getString("userinfo", "");

        txtUsername.setText(username);

        if (citystate.equals("")) {
            txtCityState.setText("Enter State");
            txtCityState.setTextColor(getResources().getColor(R.color.primary_darkest_translucent));
        } else {
            txtCityState.setText(citystate);
        }

        if (userbio.equals("")) {
            txtUserbio.setText("Enter bio");
            txtUserbio.setTextColor(getResources().getColor(R.color.primary_darkest_translucent));
        } else {
            txtUserbio.setText(userbio);
        }

        if (userinfo.equals("")) {
            txtUserinformation.setText("Enter info");
            txtUserinformation.setTextColor(getResources().getColor(R.color.primary_darkest_translucent));
        } else {
            txtUserinformation.setText(userinfo);
        }

        final TextView areaCount = (TextView) view.findViewById(R.id.txtAreaCount);

        ExternalDB dbInterface = ExternalDB.retrofit.create(ExternalDB.class);
        final Call<List<ExternalDBResponse>> call = dbInterface.getAreaCount(5.0,5,2);


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

        return view;
    }
    //should probably be renamed
    public ExternalDBResponse getNiggaCount()
    {
        final ExternalDBResponse returnResponse = new ExternalDBResponse();


        ExternalDB dbInterface = ExternalDB.retrofit.create(ExternalDB.class);
        //TODO change username to be dynamic -AB
        final Call<List<ExternalDBResponse>> call = dbInterface.getAreaCount(5.0,5,2);


        call.enqueue(new Callback<List<ExternalDBResponse>>() {
            @Override
            public void onResponse(Call<List<ExternalDBResponse>> call, Response<List<ExternalDBResponse>> response)
            {

                Log.d("response: ", response.body().get(0).getMainResponse());
                Log.d("response: ", response.body().get(0).getResponseCode());
                Log.d("response: ", response.body().get(0).getResponseMessage());
                Log.d("response: ", response.body().get(0).getEchoInput());

                returnResponse.setMainResponse(response.body().get(0).getMainResponse());
                returnResponse.setResponseCode(response.body().get(0).getResponseCode());
                returnResponse.setResponseMessage(response.body().get(0).getResponseMessage());
                returnResponse.setEchoInput(response.body().get(0).getEchoInput());

                Log.d("response: ", returnResponse.getMainResponse());
            }

            @Override
            public void onFailure(Call<List<ExternalDBResponse>> call, Throwable t) {


                Log.d("Tracker", t.getMessage());
            }
        });


        Log.d("response: test ", returnResponse.getMainResponse());
        return returnResponse;
    }
}
