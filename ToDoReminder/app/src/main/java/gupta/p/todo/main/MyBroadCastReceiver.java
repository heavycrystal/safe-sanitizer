package gupta.p.todo.main;

import android.app.Notification;
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
import android.support.annotation.RequiresApi;

import java.io.IOException;

import gupta.p.todo.R;

public class MyBroadCastReceiver extends BroadcastReceiver {

    public static MediaPlayer player;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        String name=intent.getStringExtra("TODO");
        long id=intent.getLongExtra("ID",0);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(context, (int)id, i, 0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Reminder");
        builder.setContentText(name);
        builder.setSmallIcon(R.drawable.ic_access_alarm_black_24dp);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify((int)id, notification);

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        player = new MediaPlayer();
        try {
            player.setDataSource(context, alert);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        assert audioManager != null;
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();

            if(player.isPlaying()) {
                MainActivity.playing=true;
            }
        }
    }
}
