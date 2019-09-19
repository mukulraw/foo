package com.ratna.foosip;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.Html;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by ratna on 10/18/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private Bitmap bitmap;
    private Context context = this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

         //   Toast.makeText(context,"get mEssage",Toast.LENGTH_LONG).show();

            SharedPreferences pref = getApplicationContext().getSharedPreferences(app.Config.SHARED_PREF, 0);
            String regId = pref.getString("token", null);
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

            String noti_title = remoteMessage.getNotification().getTitle();
            String [] arr = noti_title.split(",");

            if(!arr[1].equals(regId)) {
                sendUserNotification(arr[0], remoteMessage.getNotification().getBody());
            }
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());



                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }



        /*if (remoteMessage.getData().size() > 0) {
            Toast.makeText(context,"get mEssage",Toast.LENGTH_LONG).show();
            sendUserNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("text"));


        }*/

    }


    private void handleDataMessage(JSONObject data2) {
        Log.e(TAG, "push json: " + data2.toString());

        try {
            //JSONObject data = data2.getJSONObject("data");


            String type = data2.getString("type");






            if (type.equals("comment"))
            {
                JSONObject dat = data2.getJSONObject("data");


                Log.d("ddata" , dat.toString());

                Intent registrationComplete = new Intent("commentData");
                registrationComplete.putExtra("data", dat.toString());

                String json = dat.toString();

                Gson gson = new Gson();

                postListbean item = gson.fromJson(json, postListbean.class);

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

                String message = item.getSenderName() + " commented on your post";

                Log.d("notificationData", message);
                String idChannel = "southman messages";
                Intent mainIntent;

                mainIntent = new Intent(FoosipChat.getContext(), HomeChat.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(FoosipChat.getContext(), 0, mainIntent, 0);

                NotificationManager mNotificationManager = (NotificationManager) FoosipChat.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannel mChannel;
                // The id of the channel.

                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationCompat.Builder builder;

                builder = new NotificationCompat.Builder(FoosipChat.getContext(), idChannel);
                builder.setContentTitle(FoosipChat.getContext().getString(R.string.app_name))
                        .setSmallIcon(R.drawable.fooship_logo_a1)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(Html.fromHtml(message)))
                        .setAutoCancel(true)
                        .setContentText(Html.fromHtml(message));


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(idChannel, FoosipChat.getContext().getString(R.string.app_name), importance);
                    // Configure the notification channel.
                    mChannel.setDescription(FoosipChat.getContext().getString(R.string.alarm_notification));
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    if (mNotificationManager != null) {
                        mNotificationManager.createNotificationChannel(mChannel);
                    }
                } else {
                    builder.setContentTitle(FoosipChat.getContext().getString(R.string.app_name))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setColor(ContextCompat.getColor(FoosipChat.getContext(), R.color.transparent))
                            .setVibrate(new long[]{100, 250})
                            .setLights(Color.YELLOW, 500, 5000)
                            .setAutoCancel(true);
                }
                if (mNotificationManager != null) {
                    mNotificationManager.notify(1, builder.build());
                }

            }
            else if (type.equals("post"))
            {

                JSONObject dat = data2.getJSONObject("data");
                Log.d("view" , "called");

                Intent registrationComplete = new Intent("post");
                registrationComplete.putExtra("data", dat.toString());

                String json = dat.toString();

                Gson gson = new Gson();

                postListbean item = gson.fromJson(json, postListbean.class);

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

String message = "";
                if (item.getPostType().equals("ask")) {
                    message = item.getSenderName() + " Asked for a Recommendation";
                } else if (item.getPostType().equals("give")) {
                    message = item.getSenderName() + " Shared a Recommendation";
                } else if (item.getPostType().equals("moment")) {
                    message = item.getSenderName() + " Shared a Moment";
                } else if (item.getPostType().equals("food")) {
                    message = item.getSenderName() + " Shared a Food Pornography";
                }




                Log.d("notificationData", message);
                String idChannel = "southman messages";
                Intent mainIntent;

                mainIntent = new Intent(FoosipChat.getContext(), HomeChat.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(FoosipChat.getContext(), 0, mainIntent, 0);

                NotificationManager mNotificationManager = (NotificationManager) FoosipChat.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannel mChannel;
                // The id of the channel.

                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationCompat.Builder builder;

                builder = new NotificationCompat.Builder(FoosipChat.getContext(), idChannel);
                builder.setContentTitle(FoosipChat.getContext().getString(R.string.app_name))
                        .setSmallIcon(R.drawable.fooship_logo_a1)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(Html.fromHtml(message)))
                        .setAutoCancel(true)
                        .setContentText(Html.fromHtml(message));


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(idChannel, FoosipChat.getContext().getString(R.string.app_name), importance);
                    // Configure the notification channel.
                    mChannel.setDescription(FoosipChat.getContext().getString(R.string.alarm_notification));
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    if (mNotificationManager != null) {
                        mNotificationManager.createNotificationChannel(mChannel);
                    }
                } else {
                    builder.setContentTitle(FoosipChat.getContext().getString(R.string.app_name))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setColor(ContextCompat.getColor(FoosipChat.getContext(), R.color.transparent))
                            .setVibrate(new long[]{100, 250})
                            .setLights(Color.YELLOW, 500, 5000)
                            .setAutoCancel(true);
                }
                if (mNotificationManager != null) {
                    mNotificationManager.notify(1, builder.build());
                }

            }
            /*else if (type.equals("gift"))
            {

                JSONObject dat = data2.getJSONObject("data");
                Log.d("view" , "called");

                Intent registrationComplete = new Intent("gift");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("like"))
            {
                String dat = data2.getString("data");

                Log.d("view" , "called");

                Intent registrationComplete = new Intent("like");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("request"))
            {
                String dat = data2.getString("data");

                Log.d("request" , "called");

                Intent registrationComplete = new Intent("request");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("status"))
            {
                String dat = data2.getString("data");

                Log.d("status" , "called");

                Intent registrationComplete = new Intent("status");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }*/






        } catch (Exception e) {


            try {

                String text = data2.getString("text");
                String title = data2.getString("title");

                if (title.equals("Foosip Chat"))
                {

                    if (!SharePreferenceUtils.getInstance().getString("chat").equals("open"))
                    {
                        Log.d("notificationData", text);
                        String idChannel = "southman messages";
                        Intent mainIntent;

                        mainIntent = new Intent(FoosipChat.getContext(), HomeChat.class);

                        PendingIntent pendingIntent = PendingIntent.getActivity(FoosipChat.getContext(), 0, mainIntent, 0);

                        NotificationManager mNotificationManager = (NotificationManager) FoosipChat.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                        NotificationChannel mChannel;
                        // The id of the channel.

                        int importance = NotificationManager.IMPORTANCE_HIGH;

                        NotificationCompat.Builder builder;

                        builder = new NotificationCompat.Builder(FoosipChat.getContext(), idChannel);
                        builder.setContentTitle(title)
                                .setSmallIcon(R.drawable.fooship_logo_a1)
                                .setContentIntent(pendingIntent)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(Html.fromHtml(text)))
                                .setAutoCancel(true)
                                .setContentText(Html.fromHtml(text));


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mChannel = new NotificationChannel(idChannel, FoosipChat.getContext().getString(R.string.app_name), importance);
                            // Configure the notification channel.
                            mChannel.setDescription(FoosipChat.getContext().getString(R.string.alarm_notification));
                            mChannel.enableLights(true);
                            mChannel.setLightColor(Color.RED);
                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            if (mNotificationManager != null) {
                                mNotificationManager.createNotificationChannel(mChannel);
                            }
                        } else {
                            builder.setContentTitle(FoosipChat.getContext().getString(R.string.app_name))
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setColor(ContextCompat.getColor(FoosipChat.getContext(), R.color.transparent))
                                    .setVibrate(new long[]{100, 250})
                                    .setLights(Color.YELLOW, 500, 5000)
                                    .setAutoCancel(true);
                        }
                        if (mNotificationManager != null) {
                            mNotificationManager.notify(1, builder.build());
                        }
                    }
                    else
                    {

                    }

                }
                else
                {
                    Log.d("notificationData", text);
                    String idChannel = "southman messages";
                    Intent mainIntent;

                    mainIntent = new Intent(FoosipChat.getContext(), HomeChat.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(FoosipChat.getContext(), 0, mainIntent, 0);

                    NotificationManager mNotificationManager = (NotificationManager) FoosipChat.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    NotificationChannel mChannel;
                    // The id of the channel.

                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    NotificationCompat.Builder builder;

                    builder = new NotificationCompat.Builder(FoosipChat.getContext(), idChannel);
                    builder.setContentTitle(title)
                            .setSmallIcon(R.drawable.fooship_logo_a1)
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(Html.fromHtml(text)))
                            .setAutoCancel(true)
                            .setContentText(Html.fromHtml(text));


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mChannel = new NotificationChannel(idChannel, FoosipChat.getContext().getString(R.string.app_name), importance);
                        // Configure the notification channel.
                        mChannel.setDescription(FoosipChat.getContext().getString(R.string.alarm_notification));
                        mChannel.enableLights(true);
                        mChannel.setLightColor(Color.RED);
                        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        if (mNotificationManager != null) {
                            mNotificationManager.createNotificationChannel(mChannel);
                        }
                    } else {
                        builder.setContentTitle(FoosipChat.getContext().getString(R.string.app_name))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setColor(ContextCompat.getColor(FoosipChat.getContext(), R.color.transparent))
                                .setVibrate(new long[]{100, 250})
                                .setLights(Color.YELLOW, 500, 5000)
                                .setAutoCancel(true);
                    }
                    if (mNotificationManager != null) {
                        mNotificationManager.notify(1, builder.build());
                    }
                }


            }catch (Exception e1)
            {
                Log.e(TAG, "Exception1: " + e1.getMessage());
            }


            Log.e(TAG, "Exception1: " + e.getMessage());
        }
    }


    private void sendUserNotification(String title, String mess) {
        int notifyID = 1;
        Intent intent;
        NotificationChannel mChannel;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent = new Intent(context, HomeChat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = "10001";// The id of the channel.
        CharSequence name = "foosip_notification_10001";// The user-visible name of the channel.
        int importance = 0;
     //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
       // }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(mess));
        notificationBuilder.setContentText(mess);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setSmallIcon(getNotificationIcon(notificationBuilder));

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        if (notificationManager != null) {
            notificationManager.notify(notifyID /* ID of notification */, notificationBuilder.build());
        }


    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x036085;
            notificationBuilder.setColor(color);
            return R.mipmap.ic_launcher;

        } else {
            return R.mipmap.ic_launcher;
        }
    }

}