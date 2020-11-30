package com.example.snazzy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity {

    public static String USERNAME = MainActivity.USERNAME;
    public static String USEREMAIL;
    public static int PICK_FILE = 23;
    public static String dateET;
    public static String timeET;
    public static String date[];
    public static String time[];
    private static int id;
    Button buttonName;
    Button buttonDate;
    Button buttonTime, uploadDocs;
    EditText alarmName;
    String name = null;
    long mili = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        USEREMAIL = currentUser.getEmail();

        buttonName = findViewById(R.id.setAlarm);
        buttonDate = findViewById(R.id.alarmDate);
        buttonTime = findViewById(R.id.alarmTime);
        alarmName = findViewById(R.id.alarmName);
        uploadDocs = findViewById(R.id.uploadAttach);

        Intent intent = getIntent();
        String MESSAGE = intent.getStringExtra("ITEMNAME");
        if(MESSAGE!=null)
            alarmName.setHint(MESSAGE);


        if(getIntent().getIntExtra("ID", 0) != 0)
        {
            cancel_alarm(getIntent().getStringExtra("TODO"), getIntent().getIntExtra("ID", 0));
        }

        uploadDocs.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                uploadDoc();
            }
        });

    }

    public void setAlarm(View view) {

        name = alarmName.getText().toString();
        date = new String[3];
        time = new String[3];
        Random random = new Random();
        id = random.nextInt(1000);

        AlarmManager alarmManager = getSystemService(AlarmManager.class);

        Intent i = new Intent(this, NotifSender.class);
        i.putExtra("ID", id);
        i.putExtra("TODO", name);

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
        calendar.set(Integer.parseInt(date[2]),Integer.parseInt(date[1])-1 ,Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        mili = calendar.getTimeInMillis();
        calendar.setTimeInMillis(mili);

        Calendar calendarCurrent = Calendar.getInstance();
        long miliCurrent = calendarCurrent.getTimeInMillis();
        calendarCurrent.setTimeInMillis(miliCurrent);
        long diff = mili - miliCurrent;

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mili, pendingIntent);
        diff=diff/1000;
        Toast.makeText(this, "Alarm set to ring in " +diff+" secs" , Toast.LENGTH_SHORT).show();
    }

    public void showCalendar(View view) {
        Calendar calendar = Calendar.getInstance();
        final int date=calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                buttonDate.setText(""+dayOfMonth+"-"+month+"-"+year);
                dateET = ""+dayOfMonth+"-"+month+"-"+year;
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

    public void cancel_alarm(String name, int ID)
    {
        AlarmManager alarmManager = getSystemService(AlarmManager.class);

        Intent i = new Intent(this, NotifSender.class);
        i.putExtra("ID", ID);
        i.putExtra("TODO", name);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ID, i,0);
        Log.e("cancel", "me");
        alarmManager.cancel(pendingIntent);
        if((MainActivity.player != null) && (MainActivity.player.isPlaying()))
        {
            MainActivity.player.stop();
        }
    }
    public void uploadDoc(){

        name = alarmName.getText().toString();
        date = new String[3];
        time = new String[3];

        int k=0;
        for(String s: dateET.split("-")) {
            date[k++]=s;
        }
        k=0;
        for(String s: timeET.split(":")) {
            time[k++]=s;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date[2]),Integer.parseInt(date[1])-1 ,Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        mili = calendar.getTimeInMillis();
        calendar.setTimeInMillis(mili);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE);
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        // The result data contains a URI for the document or directory that
        // the user selected.
        if (resultData != null) {
            Uri docUri = resultData.getData();
            if(mili==0 || name==null)
                Toast.makeText(getApplicationContext(), "Please input data first", Toast.LENGTH_LONG).show();
            else {
                SendEmailService.getInstance(getApplicationContext()).emailExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        SendEmailService.getInstance(getApplicationContext()).SendReminderMail(USEREMAIL, USERNAME, name, docUri, getApplicationContext(), mili/1000);
                    }
                });
            }
        }
    }
}