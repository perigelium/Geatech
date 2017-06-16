package ru.alexangan.developer.geatech.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import ru.alexangan.developer.geatech.Activities.MainActivity;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;

public class NotificationIntentService extends IntentService
{

    private static final String ACTION_START = "ACTION_START";

    public NotificationIntentService()
    {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context)
    {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            String action = intent.getAction();
            if (ACTION_START.equals(action))
                processNotification();

        } finally
        {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processNotification()
    {
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        String delayHours = mSettings.getString("reminderDelayHours", "0");

        Notification nBuilder = new Notification.Builder(this)
                .setContentTitle("Non perdetevi! ")
                .setTicker("GeaTech notifica!")
                .setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.gea_logo)
                .setContentText(delayHours + " rimaste fino all'appuntamento...")
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, nBuilder);
    }
}
