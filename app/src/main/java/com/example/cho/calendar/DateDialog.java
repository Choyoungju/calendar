package com.example.cho.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by cho on 2015-04-29.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);

    }
    public void onDateSet(DatePicker view, int year, int month, int day){
        TextView dateDisplay  = (TextView) getActivity().findViewById(R.id.dateTextView);
        String date = "Note_" + year + "-" + (month+1) + "-" + day;
        dateDisplay.setText(date);
    }

}
