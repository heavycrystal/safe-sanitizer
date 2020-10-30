package com.example.simpletodo;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Integer notificationId = intent.getIntExtra("NotificationID", 0);
        SimpleTODO simpleTODO = SimpleTODO.GetTODOByID(context,notificationId.longValue());

        String intentText = intent.getAction();
        if (intentText != null) {
            switch (intentText){
                case "Done":
                    if (simpleTODO!=null)
                        simpleTODO.SetAsCompleted();
                    break;
                case "Delete":
                    if (simpleTODO!=null)
                        simpleTODO.Delete();
                    break;
                case "Close":break;
            }
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancel(notificationId);

        Intent listIntent = new Intent("ListViewDataUpdated");
        LocalBroadcastManager.getInstance(context).sendBroadcast(listIntent);
    }
}

