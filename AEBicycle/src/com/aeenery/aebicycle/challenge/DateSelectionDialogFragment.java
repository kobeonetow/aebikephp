package com.aeenery.aebicycle.challenge;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

public class DateSelectionDialogFragment extends DialogFragment{
	
	static DatePickerDialog.OnDateSetListener sListener;
	static Context sContext;
    static Calendar sDate;
    
	public static DateSelectionDialogFragment getInstance(Context context, Calendar date, DatePickerDialog.OnDateSetListener listener){
		DateSelectionDialogFragment instance = new DateSelectionDialogFragment();
	    sContext = context;
    	sDate = date;
	    sListener = listener;
	    return instance;
	}
	
 	@Override
 	public Dialog onCreateDialog(Bundle savedInstanceState) {
 		int year = sDate.get(Calendar.YEAR);
 		int month = sDate.get(Calendar.MONTH)+1;
 		int day = sDate.get(Calendar.DATE);
 		return new DatePickerDialog(getActivity(), sListener, year, month, day);
 	}

}
