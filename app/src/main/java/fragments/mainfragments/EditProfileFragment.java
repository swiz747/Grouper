package fragments.mainfragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.tritiumlabs.grouper.MainActivity;
import com.tritiumlabs.grouper.R;
import com.tritiumlabs.grouper.UserPicture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fragments.DatePickerFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by Kyle on 11/26/2016.
 */

public class EditProfileFragment extends Fragment{

    private static final int SELECT_SINGLE_PICTURE = 101;

    public static final String IMAGE_TYPE = "image/*";

    private TextView txtUserName;
    private TextView txtState;
    private TextView txtBirthday;
    private TextView txtGender;
    private TextView txtBirthDaytext;
    private EditText txtBio;
    private ImageButton btnSaveInfo;
    private ImageButton btnEditProfileImage;
    private ImageView imgProfileImage;
    private Spinner spnState;
    private SeekBar sldGender;
    private TextView txtMalePercentage;
    private TextView txtFemalePercentage;

    private int age;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_edit_profile_fragment, container, false);

        final int femalePercent = 50;
        int malePercent = 50;

        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        txtUserName = (TextView) view.findViewById(R.id.txtMainEditProfileUsername);
        txtState = (TextView) view.findViewById(R.id.txtMainEditProfileState);
        txtBirthday = (TextView) view.findViewById(R.id.txtMainEditProfileDateOfBirth);
        txtGender = (TextView) view.findViewById(R.id.txtMainEditProfileGender);
        txtBio = (EditText) view.findViewById(R.id.txtMainEditProfileBio);
        btnSaveInfo = (ImageButton) view.findViewById(R.id.btnMainEditProfileSave);
        btnEditProfileImage = (ImageButton) view.findViewById(R.id.btnMainEditEditProfileImage);
        imgProfileImage = (ImageView) view.findViewById(R.id.imgEditProfilePicture);
        txtBirthDaytext = (TextView) view.findViewById(R.id.txtMainEditProfileBirthday);
        sldGender = (SeekBar) view.findViewById(R.id.sldMainEditProfileGenderBar);
        txtMalePercentage = (TextView) view.findViewById(R.id.txtEditProfileMalePercentage);
        txtFemalePercentage = (TextView) view.findViewById(R.id.txtEditProfileFemalePercentage);

        txtUserName.setTypeface(sab);
        txtState.setTypeface(sab);
        txtBirthday.setTypeface(sab);
        txtGender.setTypeface(sab);
        txtBio.setTypeface(sab);
        txtBirthDaytext.setTypeface(sab);

        spnState = (Spinner) view.findViewById(R.id.spnState);
        String[] states = populateStatesArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnState.setAdapter(adapter);

        txtBirthday.setFocusable(false);

        sldGender.setProgress(50);
        sldGender.setMax(100);

        txtMalePercentage.setText(malePercent + "%");
        txtFemalePercentage.setText(femalePercent + "%");

        int progress = sldGender.getProgress();

        //TODO: same deal as before, don't use shared pref to obtain this data -KD
        String username = sharedPref.getString("username", "");
        String citystate = sharedPref.getString("citystate", "");
        String userbio = sharedPref.getString("userbio", "");
        String userage = String.valueOf(sharedPref.getInt("userage", 0));

        txtUserName.setText(username);

        txtBio.setText(userbio);

        try {
            ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgProfileImage = (ImageView) view.findViewById(R.id.imgEditProfilePicture);
            imgProfileImage.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        btnEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);
            }
        });

        txtBirthday.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                }
        );

        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileInfo();
            }
        });

        sldGender.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int malePercentage;
                int femalePercentage;
                progress = ((int)Math.round(progress/10))*10;
                seekBar.setProgress(progress);
                malePercentage = 100 - progress;
                femalePercentage = progress;
                txtMalePercentage.setText(malePercentage + "%");
                txtFemalePercentage.setText(femalePercentage + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context applicationContext = MainActivity.getContextOfApplication();
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {
                    imgProfileImage.setImageBitmap(new UserPicture(selectedImageUri, applicationContext.getContentResolver()).getBitmap());
                } catch (IOException e) {

                }
            }
        } else {
            // report failure
        }
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private void saveProfileInfo() {
        imgProfileImage.buildDrawingCache();
        Bitmap image = imgProfileImage.getDrawingCache();
        saveToInternalStorage(image);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("userage", age);
        editor.putString("citystate", spnState.getSelectedItem().toString());
        editor.putString("userbio", txtBio.getText().toString());

        editor.apply();
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    //TODO: This check must be made if we decide to not require the user to allow camera permissions by default through google play - as of right now it is required -KD
    /**
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
     */

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            txtBirthday.setText(String.valueOf(monthOfYear) + "/" + String.valueOf(dayOfMonth+1)
                    + "/" + String.valueOf(year));
            setAge(year);
        }
    };

    private void setAge(int birthYear) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        age = year - birthYear;
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
