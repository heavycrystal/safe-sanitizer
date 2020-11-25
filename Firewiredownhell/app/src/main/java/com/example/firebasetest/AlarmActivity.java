package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    public static String dateET;
    public static String timeET;
    public static String date[];
    public static String time[];
    Button buttonName;
    Button buttonDate;
    Button buttonTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        buttonName = findViewById(R.id.button4);
        buttonDate = findViewById(R.id.button5);
        buttonTime = findViewById(R.id.button6);
    }

    public void setAlarm(View view) {


        String name = ((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString();
        int id = 7;
        date = new String[3];
        time = new String[3];

        AlarmManager alarmManager = getSystemService(AlarmManager.class);

        Intent i = new Intent(this, NotifSender.class);
        i.putExtra("ID",id);
        i.putExtra("TODO",name);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, i,0);

        int k=0;
        for(String s: dateET.split("-")) {
            date[k++]=s;
        }
        k=0;
        for(String s: timeET.split(":")) {
            time[k++]=s;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date[2]),Integer.parseInt(date[1])-1,Integer.parseInt(date[0]),Integer.parseInt(time[0]),Integer.parseInt(time[1]));
        long mili = calendar.getTimeInMillis();
        calendar.setTimeInMillis(mili);

        Calendar calendarCurrent = Calendar.getInstance();
        long miliCurrent = calendarCurrent.getTimeInMillis();
        calendarCurrent.setTimeInMillis(miliCurrent);
        long diff = mili - miliCurrent;

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mili, pendingIntent);
        diff=diff/1000;
        Toast.makeText(this, "Alarm set to ring in" +diff+"secs" , Toast.LENGTH_SHORT).show();
    }

    public void showCalendar(View view) {
        Calendar calendar = Calendar.getInstance();
        final int date=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                buttonDate.setText(""+dayOfMonth+"-"+month+"-"+year);
                dateET=""+dayOfMonth+"-"+month+"-"+year;
            }
        },year,month,date);
        datePickerDialog.show();
    }
    public void showClock(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                buttonTime.setText(""+hourOfDay+":"+minute);
                timeET=""+hourOfDay+":"+minute;
            }
        },hour,minute,true);
        timePickerDialog.show();
    }
}