package fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tritiumlabs.grouper.R;

import java.util.Calendar;

/**
 * Created by Kyle on 11/23/2016.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        TextView tv = (TextView) getActivity().findViewById(R.id.txtGroupieTime);
        if (hourOfDay < 12) {
            tv.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + " AM");
        } else {
            int hour = hourOfDay - 12;
            tv.setText(String.valueOf(hour) + ":" + String.valueOf(minute) + " PM");
        }
    }
}