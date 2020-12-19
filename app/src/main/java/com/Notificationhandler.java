package com;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;

public class Notificationhandler extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                //Force remove push from Notification Center after 30 seconds
                //builder.setTimeoutAfter(30000);
                // Sets the icon accent color notification color to Green on Android 5.0+ devices.
                builder.setColor(new BigInteger("FF00FF00", 16).intValue());
                builder.setPriority(NotificationManager.IMPORTANCE_MAX);

                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                return builder;
            }
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);


        // Return true to stop the notification from displaying.
        return false;
    }
}

