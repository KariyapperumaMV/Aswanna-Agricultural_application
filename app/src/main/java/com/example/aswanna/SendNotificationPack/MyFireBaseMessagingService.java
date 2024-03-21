package com.example.aswanna.SendNotificationPack;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.example.aswanna.Activities.InvestorPostView;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    NotificationManager notificationManager;
    Notification notification;


    PreferenceManager preferenceManager;





    String title,message,imageUrl,location,price;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        preferenceManager = new PreferenceManager(this);
        title=remoteMessage.getData().get("projectName");
        message=remoteMessage.getData().get("projectType");
        imageUrl=remoteMessage.getData().get("imageUrl");
        location=remoteMessage.getData().get("location");
        price=remoteMessage.getData().get("price");

        NormalNotification();


    }


    private  void NormalNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent actionIntent = new Intent(this, InvestorPostView.class);
        actionIntent.putExtra("your_key", "your_value"); // You can pass data to the action

// Create a PendingIntent for the action
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

// Create an action and add it to the notification
        NotificationCompat.Action action = new NotificationCompat.Action(
                R.drawable.aswanna_logo, // Icon for the action
                "View Now", // Title of the action
                actionPendingIntent
        );

        Intent cancelIntent = new Intent(this, InvestorPostView.class);
        cancelIntent.setAction("cancel_action"); // Use a unique action string
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action cancelAction = new NotificationCompat.Action(
                R.drawable.aswanna_logo, // Icon for the cancel action
                "Dismiss", // Title of the action
                cancelPendingIntent
        );

        NotificationCompat.BigPictureStyle style =new NotificationCompat.BigPictureStyle();



        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                        .setSmallIcon(R.drawable.aswanna_logo)
                        .setContentTitle("Hi "+preferenceManager.getString(User.KEY_NAME)+",Invest Now Rs."+price+"/= For "+title)
                        .setColor(Color.GREEN)
                        .addAction(cancelAction)
                        .setContentText(message+ " Project In "+location)
                        .setSubText("New Notification From Asswanna")
                        .addAction(action);



        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

        try {
            Bitmap largeBitmap = Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get(); // This fetches the image synchronously, consider using a background thread for this operation.

            bigPictureStyle.bigPicture(largeBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        builder.setStyle(bigPictureStyle);




        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


    }







}
