package fragments.groupiefragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tritiumlabs.grouper.R;


public class CreateGroupieFragment extends Fragment {

    Button btnDatePicker;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groupies_create_fragment, container, false);

        btnDatePicker = (Button)view.findViewById(R.id.btnDatePicker);

        btnDatePicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCreateDialog(DIALOG_ID);
                    }
                }
        );

        return view;
    }

    public void showDialog(int i) {
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(getActivity(), dPickerListener, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dPickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            Toast.makeText(getActivity(), year_x + "/" + month_x + "/" + day_x, Toast.LENGTH_LONG).show();
        }
    };
}
