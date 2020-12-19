package com.example.wadagang.Extras;

import android.content.Intent;

import com.example.wadagang.MainActivity;
import com.example.wadagang.MessageActivity;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import static androidx.core.content.ContextCompat.startActivity;

public class SendNotification {

     public SendNotification(String message, String heading, String notificationKey){

         try {
             JSONObject notificationContent = new JSONObject(
                     "{'contents':{'en':'" + message + "'},"+
                             "'include_player_ids':['" + notificationKey + "']," +
                             "'headings':{'en': '" + heading + "'}" +
                             "'':{'en': '" + heading + "'}}");
             OneSignal.postNotification(notificationContent, null);
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }


}
