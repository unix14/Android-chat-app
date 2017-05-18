package com.example.android.matala3;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

interface MyOnTimeSetListener{
    void onTimeSet(int hour, int min);
    void onTimeSet(int day, int month, int year);
}

public class MyTimePicker extends EditText implements MyOnTimeSetListener {
    boolean clock = false;

//    @Override
//    public String toString(){
//        if(clock){
//            return ""+hour+":"+
//        }else{
//
//        }
//        return "";
//    }
    public MyTimePicker(Context context) {
        super(context);
        setInputType(0);
    }
    public void setClock(boolean bool){
        clock = bool;
    }
    public MyTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(0);
    }

    public MyTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputType(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN && clock){
            MyTimePickerDialog tpd =  MyTimePickerDialog.newInstance(getId());
            tpd.show(((Activity)getContext()).getFragmentManager(),"TAG");
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN &&!clock){
            MyDatePickerDialog tpd =  MyDatePickerDialog.newInstance(getId());
            tpd.show(((Activity)getContext()).getFragmentManager(),"TAG");
            return true;
        }
        return true;
    }

    @Override
    public void onTimeSet(int hour, int min) {
        if(clock)
            setText("" + hour + ":" + min);
    }

    @Override
    public void onTimeSet(int day, int month,int year) {
        if(!clock)
            setText("" + day + "/" + month+ "/" +year);
    }

    public static class MyTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private static final String ARG_CONTAINER_EDIT_TEXT_VIEW = "edit_text_container";
        MyOnTimeSetListener listener;

        public static MyTimePickerDialog newInstance(int tag) {
            MyTimePickerDialog timePickerDialog = new MyTimePickerDialog();
            Bundle args = new Bundle();
            args.putInt(ARG_CONTAINER_EDIT_TEXT_VIEW, tag);
            timePickerDialog.setArguments(args);
            return timePickerDialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            Dialog timePicker = new TimePickerDialog(getActivity(),this,22,44,false);

            if (getArguments() != null) {
                int tag = getArguments().getInt(ARG_CONTAINER_EDIT_TEXT_VIEW);
                listener = (MyOnTimeSetListener) getActivity().findViewById(tag);
            }
            return timePicker;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.onTimeSet(hourOfDay,minute);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }



    public static class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private static final String ARG_CONTAINER_EDIT_TEXT_VIEW = "edit_date_container";
        MyOnTimeSetListener listener;


        public static MyDatePickerDialog newInstance(int tag) {
            MyDatePickerDialog timePickerDialog = new MyDatePickerDialog();
            Bundle args = new Bundle();
            args.putInt(ARG_CONTAINER_EDIT_TEXT_VIEW, tag);
            timePickerDialog.setArguments(args);
            return timePickerDialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            if (getArguments() != null) {
                int tag = getArguments().getInt(ARG_CONTAINER_EDIT_TEXT_VIEW);
                listener = (MyOnTimeSetListener) getActivity().findViewById(tag);
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            Log.d("TAG","onTimeSet " + i +"." + i1+"." + i2);
            listener.onTimeSet(i2,i1,i);
        }
    }
}



