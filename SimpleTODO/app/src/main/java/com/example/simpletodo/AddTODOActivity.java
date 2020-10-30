package com.example.simpletodo;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTODOActivity extends AppCompatActivity{

    Button buttonSave, buttonCancel;
    EditText textTitle, textDescription, textDue, textTimeDue;
    DatePickerDialog dateDue;
    TimePickerDialog timeDue;
    Date dueDate, dueTime;
    boolean isUpdate = false;
    SimpleTODO simpleTODO;

    private SimpleDateFormat dateFormatter, timeFormatter;

    RelativeLayout holder;
    AnimationDrawable anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        holder = (RelativeLayout) findViewById(R.id.activity_add_todo);

        anim = (AnimationDrawable) holder.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);

        Calendar newCalendar = Calendar.getInstance();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);

        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        textTitle = (EditText) findViewById(R.id.editTextTitle);
        textDescription = (EditText) findViewById(R.id.editTextDescription);
        textDue = (EditText) findViewById(R.id.datePickerDueDate);
        textTimeDue = (EditText) findViewById(R.id.timePickerDueTime);

        textDue.setText(dateFormatter.format(newCalendar.getTime()));
        textTimeDue.setText(timeFormatter.format(newCalendar.getTime()));
        dueDate = dueTime = newCalendar.getTime();

        Intent intentItem = getIntent();
        simpleTODO = (SimpleTODO) intentItem.getSerializableExtra("data");
        if (simpleTODO != null){
            isUpdate = true;
            textTitle.setText(simpleTODO.getTitle());
            textDescription.setText(simpleTODO.getDescription());
            newCalendar.setTime(simpleTODO.getDue());
            /* newCalendar.set(simpleTODO.getDue().getYear(),simpleTODO.getDue().getMonth()-1,simpleTODO.getDue().getDay(),simpleTODO.getDue().getHours(),simpleTODO.getDue().getMinutes()); */
            textDue.setText(dateFormatter.format(newCalendar.getTime()));
            textTimeDue.setText(timeFormatter.format(newCalendar.getTime()));
            dueDate = dueTime = newCalendar.getTime();
            textDescription.setEnabled(true);
            textDue.setEnabled(true);
            textTimeDue.setEnabled(true);
            buttonSave.setEnabled(true);
            ((TextView) findViewById(R.id.textView)).setText(R.string.update_todo);
        }

        textTitle.addTextChangedListener(new TextWatcher() {
                                             @Override
                                             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                             }

                                             @Override
                                             public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                 if(textTitle.getText().length()>0){
                                                     buttonSave.setEnabled(true);
                                                     textDue.setEnabled(true);
                                                     textDescription.setEnabled(true);
                                                     textTimeDue.setEnabled(true);
                                                 }
                                                 else{
                                                     buttonSave.setEnabled(false);
                                                     textDue.setEnabled(false);
                                                     textDescription.setEnabled(false);
                                                     textTimeDue.setEnabled(false);
                                                 }
                                             }

                                             @Override
                                             public void afterTextChanged(Editable s) {
                                             }
                                         }
        );

        textDue.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateDue.show();
                    }
                }
        );

        textTimeDue.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeDue.show();
                    }
                }
        );

        timeDue = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newDateTime = Calendar.getInstance();
                newDateTime.set(newDateTime.get(Calendar.YEAR), newDateTime.get(Calendar.MONTH), newDateTime.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
                textTimeDue.setText(timeFormatter.format(newDateTime.getTime()));
                dueTime = newDateTime.getTime();
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);


        dateDue = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                textDue.setText(dateFormatter.format(newDate.getTime()));
                dueDate = newDate.getTime();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        buttonCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        buttonSave.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        String title = textTitle.getText().toString();
                        String description = textDescription.getText().toString();

                        Calendar reminderDate = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(dueDate));
                        @SuppressLint("SimpleDateFormat") int month = Integer.parseInt(new SimpleDateFormat("MM").format(dueDate));
                        @SuppressLint("SimpleDateFormat") int day = Integer.parseInt(new SimpleDateFormat("dd").format(dueDate));
                        @SuppressLint("SimpleDateFormat") int hour = Integer.parseInt(new SimpleDateFormat("HH").format(dueTime));
                        @SuppressLint("SimpleDateFormat") int minutes = Integer.parseInt(new SimpleDateFormat("mm").format(dueTime));
                        reminderDate.set(year,month-1,day,hour,minutes);
                        Date due  =  reminderDate.getTime(); //dueDate;

                        boolean operationResult;
                        if(!isUpdate){
                            simpleTODO = new SimpleTODO(AddTODOActivity.this, title, description, due);
                            operationResult = simpleTODO.Save();
                        }
                        else{
                            simpleTODO.setTitle(title);
                            simpleTODO.setDescription(description);
                            simpleTODO.setDue(due);
                            operationResult = simpleTODO.Update();
                        }
                        if (operationResult) {
                            Toast.makeText(AddTODOActivity.this, "TODO Successfully Saved!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent("ListViewDataUpdated");
                            LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(intent);


                            /* ----Start Code: Set Local Notification---- */
                            int id =  simpleTODO.getId().intValue();

                            Calendar notificationReminder = Calendar.getInstance();
                            notificationReminder.set(year,month,day,hour,minutes);
                            Date reminder = notificationReminder.getTime();

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                            Intent notificationIntent = new Intent(getBaseContext(),NotificationBroadcastReceiver.class);

                            notificationIntent.putExtra("Notification", buildNotification(title,description,id));
                            notificationIntent.putExtra("NotificationDate",reminder);
                            notificationIntent.putExtra("NotificationID",id);

                            PendingIntent broadcast = PendingIntent.getBroadcast(getBaseContext(), id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.clear();
                            cal.set(Calendar.YEAR,year);
                            cal.set(Calendar.MONTH,month-1);
                            cal.set(Calendar.DATE,day);
                            cal.set(Calendar.HOUR_OF_DAY,hour);
                            cal.set(Calendar.MINUTE,minutes);

                            Date tempDate = cal.getTime();

                            Objects.requireNonNull(alarmManager).set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

                            /* ----End Code: Set Local Notification---- */

                            finish();
                        } else {
                            Toast.makeText(AddTODOActivity.this, "TODO Not Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }

    private Notification buildNotification(String Title, String Description, Integer ID){
        Intent notificationIntent = new Intent(this, MyTODOsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MyTODOsActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("TODO: "+Title);
        if (Description.length()!=0) {
            builder.setContentText(Description);
            builder.setStyle(new Notification.BigTextStyle().bigText(Description));
        }
        builder.setSmallIcon(android.R.drawable.star_on);
        builder.setTicker("Reminder!");
        builder.setVibrate(new long[]{2500, 2500});
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        Intent actionIntent1 = new Intent(this,NotificationActionReceiver.class);
        actionIntent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        actionIntent1.setAction("Done");
        actionIntent1.putExtra("NotificationID",ID);
        PendingIntent actionPeningIntent1 = PendingIntent.getBroadcast(this,ID+1,actionIntent1,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_notification_done,"Done!",actionPeningIntent1);

        Intent actionIntent2 = new Intent(this,NotificationActionReceiver.class);
        actionIntent2.setAction("Delete");
        actionIntent2.putExtra("NotificationID",ID);
        actionIntent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent actionPeningIntent2 = PendingIntent.getBroadcast(this,ID+2,actionIntent2,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_notification_delete,"Delete",actionPeningIntent2);

        Intent actionIntent3 = new Intent(this,NotificationActionReceiver.class);
        actionIntent3.setAction("Close");
        actionIntent3.putExtra("NotificationID",ID);
        actionIntent3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent actionPeningIntent3 = PendingIntent.getBroadcast(this,ID+3,actionIntent3,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_notification_close,"Close",actionPeningIntent3);

        return builder.build();
    }

}
