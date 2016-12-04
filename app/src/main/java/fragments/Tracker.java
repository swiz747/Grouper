package fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tritiumlabs.grouper.Friend;
import com.tritiumlabs.grouper.LocationInfo;
import com.tritiumlabs.grouper.LocationRetriever;
import com.tritiumlabs.grouper.MyService;
import com.tritiumlabs.grouper.R;
import java.util.ArrayList;
import java.util.List;

import fragments.mainfragments.GroupiesFragment;
import interfaces.ExternalDB;
import objects.ExternalDBResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tritiumlabs.grouper.MyXMPP.dbHandler;

public class Tracker extends android.support.v4.app.Fragment {

    private static final String MAP_FRAGMENT_TAG = "map";
    private LocationRetriever locationRetriever;
    private ProgressDialog progress;
    private String myLatitudeText;
    private String myLongitudeText;
    private Double myLatitude;
    private Double myLongitude;
    private GoogleMap googleMap;
    MapView mapView;

    public Tracker.TouchableWrapper mTouchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groupie Map");
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(v);

        progress = new ProgressDialog(getActivity());
        locationRetriever = LocationRetriever.getInstance(getActivity());
        AsyncTask<Void, Void, Boolean> retrieveLocation = new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setMessage("Getting Location");
                progress.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids)
            {
                //TODO add timeout -AB
                //TODO there HAS to be a better way to do this but ive spent over 96 hours working on this fucker so this is how its working for now god damnit -AB
                while(!locationRetriever.getIsDataReady())
                {

                }
                Log.d("nigga","in background thread");
                //googleMap.addMarker(new MarkerOptions().position(new LatLng(21,46)).title("me").snippet("holy shit, will this work?"));

                //addFriendMarkers();

                return null;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                Log.d("nigga",locationRetriever.getCombinedLatLong());
                myLatitudeText = locationRetriever.getLatitude();
                myLongitudeText = locationRetriever.getLongitude();
                myLatitude = Double.valueOf(locationRetriever.getLatitude());
                myLongitude = Double.valueOf(locationRetriever.getLongitude());
                updateLocation(myLatitude, myLongitude);
                progress.dismiss();
                setMapCamera();
            }
        };
        retrieveLocation.execute();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;

                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

            }
        });

        MapsInitializer.initialize(this.getActivity());

        return mTouchView;
    }
    public void setMapCamera()
    {

        Log.d("nigga", Double.toString(myLatitude) + " " + Double.toString(myLatitude));
        LatLng myLocation = new LatLng(myLatitude, myLongitude);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.addMarker(new MarkerOptions().position(myLocation).title("me").snippet("holy shit, will this work?"));

    }

    public void updateLocation(double latitude, double longitude)
    {
        ExternalDB dbInterface = ExternalDB.retrofit.create(ExternalDB.class);
        //TODO change username to be dynamic -AB
        final Call<List<ExternalDBResponse>> call =
                dbInterface.setLocation(dbHandler.getUsername(), latitude, longitude);

        call.enqueue(new Callback<List<ExternalDBResponse>>() {
            @Override
            public void onResponse(Call<List<ExternalDBResponse>> call, Response<List<ExternalDBResponse>> response) {


            }

            @Override
            public void onFailure(Call<List<ExternalDBResponse>> call, Throwable t) {


                Log.d("Tracker", t.getMessage());
            }
        });
    }
    
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    GroupiesFragment.disableScroll();
                    Log.v("Map Holder", "Scroll Disabled");
                    break;

                case MotionEvent.ACTION_UP:
                    GroupiesFragment.enableScroll();
                    Log.v("Map Holder", "Scroll Enabled");
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }
}

