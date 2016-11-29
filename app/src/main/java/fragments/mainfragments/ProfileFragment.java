package fragments.mainfragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tritiumlabs.grouper.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.R.attr.path;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        setHasOptionsMenu(true);
    }

    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_RESOURCE_ID = "itemID";

    TextView txtUserName;
    TextView txtState;
    TextView txtAgeGender;
    TextView txtUserBio;
    ImageButton btnEditProfile;
    ImageView imgProfilePicture;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_profile_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        txtUserName = (TextView) view.findViewById(R.id.txtMainProfileUsername);
        txtState = (TextView) view.findViewById(R.id.txtMainProfileState);
        txtAgeGender = (TextView) view.findViewById(R.id.txtMainProfileAgeGender);
        txtUserBio = (TextView) view.findViewById(R.id.txtMainProfileBio);
        btnEditProfile = (ImageButton) view.findViewById(R.id.btnMainProfileEditProfile);

        txtUserName.setTypeface(sab);
        txtState.setTypeface(sab);
        //txtAgeGender.setTypeface(sab);
        txtUserBio.setTypeface(sab);

        //TODO: same deal as before, don't use shared pref to obtain this data -KD
        String username = sharedPref.getString("username", "");
        String citystate = sharedPref.getString("citystate", "");
        String userbio = sharedPref.getString("userbio", "");
        String userage = String.valueOf(sharedPref.getInt("userage", 0));


        txtUserName.setText(username);

        if (citystate.equals("")) {
            txtState.setText("");
        } else {
            txtState.setText(citystate);
        }

        if (userbio.equals("")) {
            txtUserBio.setText("");
        } else {
            txtUserBio.setText(userbio);
        }

        if (userage.equals("")) {
            txtAgeGender.setText("");
        } else {
            txtAgeGender.setText(userage + ", Male");
        }

        try {
            ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgProfilePicture = (ImageView) view.findViewById(R.id.imgProfilePicture);
            imgProfilePicture.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfile();
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_profile_menu, menu);
    }

    private void openEditProfile() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        EditProfileFragment editProfileFragment = new EditProfileFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerMain, editProfileFragment, "EditProfileFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private String[] populateStatesArray() {
        String[] states = new String [] {
                "Alaska",
                "Alabama",
                "Arkansas",
                "Arizona",
                "California",
                "Colorado",
                "Connecticut",
                "District of Columbia",
                "Delaware",
                "Florida",
                "Georgia",
                "Guam",
                "Hawaii",
                "Iowa",
                "Idaho",
                "Illinois",
                "Indiana",
                "Kansas",
                "Kentucky",
                "Louisiana",
                "Massachusetts",
                "Maryland",
                "Maine",
                "Michigan",
                "Minnesota",
                "Missouri",
                "Mississippi",
                "Montana",
                "North Carolina",
                "North Dakota",
                "Nebraska",
                "New Hampshire",
                "New Jersey",
                "New Mexico",
                "Nevada",
                "New York",
                "Ohio",
                "Oklahoma",
                "Oregon",
                "Pennsylvania",
                "Rhode Island",
                "South Carolina",
                "South Dakota",
                "Tennessee",
                "Texas",
                "Utah",
                "Virginia",
                "Virgin Islands",
                "Vermont",
                "Washington",
                "Wisconsin",
                "West Virginia",
                "Wyoming"
        };
        return states;
    }


}
