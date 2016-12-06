package fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.tritiumlabs.grouper.LocationRetriever;
import com.tritiumlabs.grouper.R;

import java.util.List;

import fragments.mainfragments.GroupiesFragment;
import interfaces.ExternalDB;
import objects.ExternalDBResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tritiumlabs.grouper.MyXMPP.dbHandler;



//TODO Read the multi line comment below -AB

/**
 *
 * ok, we have an issue with this class, its plagued with timing problems and poor design,
 * completely my fault but i believe i have the solution.
 *
 * currently the design method is to start an async task that acquires the current location
 * right before we create the map. The idea was to "initialize" the map with the current location
 * and a handful of test markers when we create it. That turned out to be a really bad plan and it
 * lead to some seriously shitty issues.
 *
 * first, lets just create the map, dont worry about markers or fucking anything, just
 * make sure the map gets fucking created then lets start fiddling with map markers and location bullshit.
 *
 * next is how were going to retrieve user location, we still want that fancy zoom thing but we dont
 * need that the absolute nanosecond the map gets created, the very minor delay wont inhibit user experience
 *
 * to do this we will need to use something called requestLocationUpdates, tehre are a few different versions
 * of this method but ive got it narrowed down a bit it will be one of these two but im leaning
 * heavily towards the first one
 *
 * public abstract PendingResult<Status> requestLocationUpdates (GoogleApiClient client, LocationRequest request, PendingIntent callbackIntent)
 *
 * public abstract PendingResult<Status> requestLocationUpdates (GoogleApiClient client, LocationRequest request, LocationListener listener)
 *
 * you can read more about all this complicated wizardry here https://goo.gl/YRn1ac
 *
 *
 */

public class Tracker extends android.support.v4.app.Fragment {

    private static final String MAP_FRAGMENT_TAG = "map";
    private LocationRetriever locationRetriever;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
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

        locationRetriever = new LocationRetriever(getActivity());
        mLocationListener = new LocalLocationListener();
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, mLocationListener);

        Log.e("Tracker", "onCreateView: created mapfrag" );

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;
                addGroupiesToMap();
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
                googleMap.setMyLocationEnabled(true);

            }
        });

        MapsInitializer.initialize(this.getActivity());

        return mTouchView;
    }
    public void setMapCamera(LatLng myLocation)
    {

        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.addMarker(new MarkerOptions().position(myLocation).title("me").snippet("holy shit, will this work?"));


    }

    public void addGroupiesToMap()
    {
        LatLng warwick = new LatLng(41.7, -71.4162);
        LatLng vinny = new LatLng(41.700141, -71.385899);
        LatLng olivia = new LatLng(41.711241, -71.443894);
        LatLng abby = new LatLng(41.701038, -71.448349);
        LatLng abbyDad = new LatLng(41.754445, -71.426363);

        googleMap.addMarker(new MarkerOptions().position(warwick).title("Warwick").snippet("Warwick Test Marker"));
        googleMap.addMarker(new MarkerOptions().position(vinny).title("Vinny").snippet("Vinny Test Marker"));
        googleMap.addMarker(new MarkerOptions().position(olivia).title("Lublub").snippet("Lublub test marker"));
        googleMap.addMarker(new MarkerOptions().position(abby).title("Abby Mom").snippet("Abby Test marker"));
        googleMap.addMarker(new MarkerOptions().position(abbyDad).title("Abby Dad").snippet("Abby Test marker"));
    }

    public void updateLocation(LatLng myLocation)
    {
        ExternalDB dbInterface = ExternalDB.retrofit.create(ExternalDB.class);
        //TODO change username to be dynamic -AB
        final Call<List<ExternalDBResponse>> call =
                dbInterface.setLocation(dbHandler.getUsername(), myLocation.latitude, myLocation.longitude);

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
    public void onStop() {
        super.onStop();
        mapView.onStop();
        locationRetriever.onStop();
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Get extra data included in the Intent
            Double latitude = intent.getDoubleExtra("latitude", 0.0);
            Double longitude = intent.getDoubleExtra("longitude", 0.0);
            String location = "Latitude " + String.valueOf(latitude) + ":" + "Longitude " + String.valueOf(longitude);
            Log.e("Tracker", "onReceive: " + location);
        }
    };

    private class LocalLocationListener implements LocationListener
    {
        String TAG= "LOCALLOCATIONLISTENER";

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: ");
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.e(TAG, "onStatusChanged: ");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.e(TAG, "onProviderEnabled: ");
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.e(TAG, "onProviderDisabled: ");
        }
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
                    Log.v(MAP_FRAGMENT_TAG, "Scroll Disabled");
                    break;

                case MotionEvent.ACTION_UP:
                    GroupiesFragment.enableScroll();
                    Log.v(MAP_FRAGMENT_TAG, "Scroll Enabled");
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }
}

