package com.example.snazzy;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class NotifSender extends BroadcastReceiver {

    public static MediaPlayer player;
    private static final String CHANNEL_ID = "421";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle(intent.getStringExtra("TODO"));
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);

        Intent i = new Intent(context, AlarmActivity.class);
        i.putExtra("TODO", intent.getStringExtra("TODO"));
        i.putExtra("ID", intent.getIntExtra("ID", 0));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(context, intent.getIntExtra("ID", 0), i, 0);
        builder.setColor(0XFFFF007F);
        builder.setContentIntent(pendingIntent);
        builder.setContentText("Tap on notification to cancel");

        createNotificationChannel(context);
        Notification notification = builder.build();
        notification.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.notify(intent.getIntExtra("ID", 7), notification);

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        MainActivity.player = new MediaPlayer();
        try {
            MainActivity.player.setDataSource(context, alert);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            MainActivity.player.setAudioStreamType(AudioManager.STREAM_ALARM);
            MainActivity.player.setLooping(true);
            try {
                MainActivity.player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainActivity.player.start();

        }
    }

    private static void createNotificationChannel(Context ctx) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            String description = "helpme";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}