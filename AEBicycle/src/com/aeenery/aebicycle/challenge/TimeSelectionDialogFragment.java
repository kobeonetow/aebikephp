package com.aeenery.aebicycle.challenge;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class TimeSelectionDialogFragment extends DialogFragment{
	
	static OnTimeSetListener sListener;
	static Context sContext;
    static Calendar sDate;
    
	public static TimeSelectionDialogFragment getInstance(Context context, Calendar date, OnTimeSetListener listener){
		TimeSelectionDialogFragment instance = new TimeSelectionDialogFragment();
	    sContext = context;
    	sDate = date;
	    sListener = listener;
	    return instance;
	}
	
 	@Override
 	public Dialog onCreateDialog(Bundle savedInstanceState) {
 		int hour = sDate.get(Calendar.HOUR_OF_DAY);
 		Log.i("Hour",hour+"");
 		int minute = sDate.get(Calendar.MINUTE);
 		TimePickerDialog dialog =  new TimePickerDialog(sContext, sListener, hour, minute, true);
 		return dialog;
 	}
}
