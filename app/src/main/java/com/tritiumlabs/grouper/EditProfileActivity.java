package com.tritiumlabs.grouper;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.tritiumlabs.grouper.adapters.MainDrawerAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import fragments.DatePickerFragment;
import fragments.FriendAddFragment;
import fragments.FriendsListFragment;
import fragments.mainfragments.FriendsFragment;
import fragments.mainfragments.GroupChatFragment;
import fragments.mainfragments.GroupiesFragment;
import fragments.mainfragments.HomeFragment;
import fragments.mainfragments.InboxFragment;
import fragments.mainfragments.ProfileFragment;
import interfaces.ExternalDB;
import objects.ExternalDBResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

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

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor prefEditor;

    private int age;
    private int fragmentIndex = 0;
    private String[] statesArray = populateStatesArray();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    MainDrawerAdapter adapter;
    List<MainDrawerItem> drawerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_profile_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editProfileToolbar);
        setSupportActionBar(toolbar);

        drawerDataList = new ArrayList<MainDrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.editProfile_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.edit_profile_left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerDataList.add(new MainDrawerItem("Home", R.drawable.homebutton));
        drawerDataList.add(new MainDrawerItem("Inbox", R.drawable.inbox_button));
        drawerDataList.add(new MainDrawerItem("FriendS", R.drawable.friends_button));
        drawerDataList.add(new MainDrawerItem("Group Chat", R.drawable.group_chat_button));
        drawerDataList.add(new MainDrawerItem("Profile", R.drawable.profile_button));
        drawerDataList.add(new MainDrawerItem("Groupies", R.drawable.groupie_button));
        drawerDataList.add(new MainDrawerItem("Settings", R.drawable.settings_button));
        drawerDataList.add(new MainDrawerItem("Logout", R.drawable.logout_button));

        adapter = new MainDrawerAdapter(this, R.layout.custom_drawer_item, drawerDataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        sharedPref = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        prefEditor = sharedPref.edit();

        Typeface sab = Typeface.createFromAsset(this.getAssets(), "Sabandija-font-ffp.ttf");

        txtUserName = (TextView) findViewById(R.id.txtEditProfileActivityUsername);
        txtState = (TextView) findViewById(R.id.txtEditProfileActivityState);
        txtBirthday = (TextView) findViewById(R.id.txtEditProfileActivityDateOfBirth);
        txtGender = (TextView) findViewById(R.id.txtEditProfileActivityGender);
        txtBio = (EditText) findViewById(R.id.txtEditProfileActivityProfileBio);
        btnSaveInfo = (ImageButton) findViewById(R.id.btnEditProfileActivitySave);
        btnEditProfileImage = (ImageButton) findViewById(R.id.btnEditProfileActivityEditProfileImage);
        imgProfileImage = (ImageView) findViewById(R.id.imgEditProfileActivityProfilePicture);
        txtBirthDaytext = (TextView) findViewById(R.id.txtEditProfileActivityBirthday);
        sldGender = (SeekBar) findViewById(R.id.sldEditProfileActivityGenderBar);
        txtMalePercentage = (TextView) findViewById(R.id.txtEditProfileActivityMalePercentage);
        txtFemalePercentage = (TextView) findViewById(R.id.txtEditProfileActivityFemalePercentage);

        txtUserName.setTypeface(sab);
        txtState.setTypeface(sab);
        //txtBirthday.setTypeface(sab);
        txtGender.setTypeface(sab);
        txtBio.setTypeface(sab);
        txtBirthDaytext.setTypeface(sab);

        spnState = (Spinner) findViewById(R.id.spnEditProfileActivityState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, statesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnState.setAdapter(adapter);
        spnState.setSelection(sharedPref.getInt("stateindex", 0));

        txtBirthday.setFocusable(false);
        sldGender.setMax(100);

        int progress = sldGender.getProgress();

        //TODO: same deal as before, don't use shared pref to obtain this data -KD
        String username = sharedPref.getString("username", "");
        String userbio = sharedPref.getString("userbio", "");
        String userage = String.valueOf(sharedPref.getInt("userage", 0));
        String userbirthday = sharedPref.getString("userbirthday", "");
        int userMalePercent = sharedPref.getInt("malepercent", 0);
        int userFemalePercent = sharedPref.getInt("femalepercent", 0);
        age = sharedPref.getInt("userage", 0);

        int malePercent = userMalePercent;
        int femalePercent = userFemalePercent;

        txtBirthday.setText(userbirthday);
        sldGender.setProgress(userFemalePercent);
        txtMalePercentage.setText(malePercent + "%");
        txtFemalePercentage.setText(femalePercent + "%");

        txtUserName.setText(username);

        txtBio.setText(userbio);

        try {
            ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgProfileImage = (ImageView) findViewById(R.id.imgEditProfileActivityProfilePicture);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("open-fragment");
        // You can also include some extra data.
        intent.putExtra("message", fragmentIndex);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendMessage();
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
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
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
        //TODO just added this hot shit right hynah -AB
        saveToExternalStorage(image);

        String malePercentage = txtMalePercentage.getText().toString();
        String femalePercentage = txtFemalePercentage.getText().toString();

        malePercentage = malePercentage.replaceAll("[^\\d.]", "");
        femalePercentage = femalePercentage.replaceAll("[^\\d.]", "");

        Log.d("Edit frag", txtBirthday.getText().toString());

        prefEditor.putInt("userage", age);
        prefEditor.putString("state", spnState.getSelectedItem().toString());
        prefEditor.putInt("stateindex", spnState.getSelectedItemPosition());
        prefEditor.putString("userbirthday", txtBirthday.getText().toString());
        prefEditor.putString("userbio", txtBio.getText().toString());
        prefEditor.putInt("malepercent", Integer.valueOf(malePercentage));
        prefEditor.putInt("femalepercent", Integer.valueOf(femalePercentage));

        prefEditor.apply();

    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
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

    //TODO the php works, we just need to use my function to upload -AB
    private void saveToExternalStorage(Bitmap bitmapImage)
    {
        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File file=new File(directory,"profile.jpg");

        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("fileToUpload", file.getName(), fbody);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "usernameHere");
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "type");

        ExternalDB dbInterface = ExternalDB.retrofit.create(ExternalDB.class);
        final Call<List<ExternalDBResponse>> call = dbInterface.uploadProfilePicture(username, type, image);

        call.enqueue(new Callback<List<ExternalDBResponse>>() {
            @Override
            public void onResponse(Call<List<ExternalDBResponse>> call, Response<List<ExternalDBResponse>> response)
            {
                Log.d("response: ", response.toString());
                Log.d("response: ", response.message());
                Log.d("response: ", (Integer.toString(response.code())));

                Log.d("response: ", response.body().get(0).getMainResponse());
                Log.d("response: ", response.body().get(0).getResponseCode());
                Log.d("response: ", response.body().get(0).getResponseMessage());
                Log.d("response: ", response.body().get(0).getEchoInput());
            }

            @Override
            public void onFailure(Call<List<ExternalDBResponse>> call, Throwable t)
            {
                Log.d("Tracker", t.getMessage());
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            txtBirthday.setText(String.valueOf(monthOfYear+1) + "/" + String.valueOf(dayOfMonth)
                    + "/" + String.valueOf(year));
            setAge(year);
        }
    };

    private void setAge(int birthYear) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        age = year - birthYear;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            fragmentIndex = position;
            finish();
        }
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


