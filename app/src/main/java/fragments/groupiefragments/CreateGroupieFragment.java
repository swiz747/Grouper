package fragments.groupiefragments;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import java.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tritiumlabs.grouper.R;

import fragments.DatePickerFragment;
import fragments.TimePickerFragment;

public class CreateGroupieFragment extends Fragment {

    EditText txtDescription;
    EditText txtName;
    EditText txtLocation;
    EditText txtDate;
    EditText txtTime;
    ImageButton btnCreate;

    private int year, month, day;

    static final int DIALOG_ID = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groupies_create_fragment, container, false);

        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        txtDescription = (EditText)view.findViewById(R.id.txtGroupieDescription);
        txtName = (EditText)view.findViewById(R.id.txtGroupieName);
        txtLocation = (EditText)view.findViewById(R.id.txtGroupieLocation);
        txtDate = (EditText)view.findViewById(R.id.txtGroupieDate);
        txtTime = (EditText)view.findViewById(R.id.txtGroupieTime);

        btnCreate = (ImageButton)view.findViewById(R.id.btnGroupieGroupIt);

        txtDescription.setTypeface(sab);
        txtName.setTypeface(sab);
        txtLocation.setTypeface(sab);

        //TODO: Buy this font damnit. -KD
        //TODO: I will soz <3 -AB
        //txtDate.setTypeface(sab);
        //txtTime.setTypeface(sab);

        txtDate.setFocusable(false);
        txtTime.setFocusable(false);

        txtDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                }
        );

        txtTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTimePicker();
                    }
                }
        );

        btnCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                //TODO add input validation to make sure they didnt leave anything blank, the server gets angry if that happens and it hits me when its angry :( -AB

            }
        });

        return view;
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
        date.show(getFragmentManager(), "Date Picker");
    }


    private void showTimePicker() {
        TimePickerFragment time = new TimePickerFragment();
        time.show(getFragmentManager(), "TimePicker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            txtDate.setText(String.valueOf(monthOfYear+1) + "/" + String.valueOf(dayOfMonth)
                    + "/" + String.valueOf(year));
        }
    };
}
