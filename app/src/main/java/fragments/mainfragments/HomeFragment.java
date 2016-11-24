package fragments.mainfragments;

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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_home_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");


        final TextView areaCount = (TextView) view.findViewById(R.id.txtAreaCount);
        ExternalDB dbInterface = ExternalDB.retrofit.create(ExternalDB.class);

        //TODO pass in users current lat/long and the size of grid for searching -AB
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

}
