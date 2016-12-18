package fragments.mainfragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.tritiumlabs.grouper.CustomScrollView;
import com.tritiumlabs.grouper.GroupieActivity;
import com.tritiumlabs.grouper.Manifest;
import com.tritiumlabs.grouper.R;

import fragments.MapHolder;
import fragments.Tracker;

public class GroupiesFragment extends Fragment {

    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_RESOURCE_ID = "itemID";
    public static final String FRAG_POSITION = "position";
    private final String TAG = "GroupiesFragment";

    public static boolean mMapIsTouched = false;

    ImageView activeFragIcon;
    TextView txtFindGroupiesText;
    TextView txtCreateOwnGroupieText;
    TextView txtCreateGroupie;
    TextView txtEditGroupie;
    TextView txtDeleteGroupie;
    TextView getTxtEditGroupie;
    FrameLayout fragMapHolder;
    public static CustomScrollView scrGroupiesFragment;

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_groupies_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groupies");

        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        txtCreateGroupie = (TextView) view.findViewById(R.id.txtMainGroupiesFragmentCreateGroupie);
        txtEditGroupie = (TextView)  view.findViewById(R.id.txtMainGroupiesFragmentEditGroupie);
        txtDeleteGroupie = (TextView) view.findViewById(R.id.txtMainGroupiesFragmentDeleteGroupie);
        txtCreateOwnGroupieText = (TextView) view.findViewById(R.id.txtMainGroupieFragmentFindGroupiesText);
        txtFindGroupiesText = (TextView) view.findViewById(R.id.txtMainGroupieFragmentManageroupiesText);
        scrGroupiesFragment = (CustomScrollView) view.findViewById(R.id.scrMainGroupiesFragment);
        fragMapHolder = (FrameLayout) view.findViewById(R.id.fragMainGroupieFragmentMapContainer);

        txtCreateGroupie.setTypeface(sab);
        txtEditGroupie.setTypeface(sab);
        txtDeleteGroupie.setTypeface(sab);
        txtCreateOwnGroupieText.setTypeface(sab);
        txtFindGroupiesText.setTypeface(sab);

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        txtCreateGroupie.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCreateGroupiesFragment();
                    }
                }
        );
        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    createMapFragment();
                    Log.d(TAG, "did this do anything");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static void enableScroll() {
        scrGroupiesFragment.setEnableScrolling(true);
    }

    public static void disableScroll() {
        scrGroupiesFragment.setEnableScrolling(false);
    }

    private void createMapFragment() {
        Fragment trackerFragment = new Tracker();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragMainGroupieFragmentMapContainer, trackerFragment).commit();
    }

    private void openCreateGroupiesFragment() {
        Intent intent = new Intent(getActivity(), GroupieActivity.class);
        intent.putExtra("action", "create");
        startActivity(intent);
    }
}