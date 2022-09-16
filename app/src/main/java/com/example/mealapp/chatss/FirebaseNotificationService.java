package com.example.mealapp.chatss;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.mealapp.Acceptances.ChatsViewActivity;
import com.example.mealapp.Constants.AllConstants;
import com.example.mealapp.Main.MainActivity;
import com.example.mealapp.R;
import com.example.mealapp.UserPickup.UserPickupsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FirebaseNotificationService extends com.google.firebase.messaging.FirebaseMessagingService {
    String title,message,userIdFromIntent,chatId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getData().size() > 0){
            Map<String,String> map = remoteMessage.getData();

            if(map.get("chatId") != null){
                title = map.get("title");
                message = map.get("message");
                userIdFromIntent = map.get("userIdFromIntent");
                chatId  = map.get("chatId");


                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
                    createOreoNotification(title,message,userIdFromIntent,chatId);
                }
                else{
                    createNormalNotification(title,message,userIdFromIntent,chatId);
                }

            }
            else{

                title = map.get("title");
                message = map.get("message");
                userIdFromIntent = map.get("userIdFromIntent");


                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
                    createOreoNotification(title,message,userIdFromIntent);
                }
                else{
                    createNormalNotification(title,message,userIdFromIntent);
                }


            }






        }
        super.onMessageReceived(remoteMessage);
    }

    private void createNormalNotification(String title, String message, String userIdFromIntent) {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AllConstants.CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(getResources(),R.color.pastel_blue,null))
                .setSound(uri);

        Intent intent= new Intent(this, UserPickupsActivity.class);
        intent.putExtra("userIdFromIntent",userIdFromIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(85-65),builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOreoNotification(String title, String message, String userIdFromIntent) {

        NotificationChannel channel= new NotificationChannel(AllConstants.CHANNEL_ID,"Message",NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Message Description");
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Intent intent = new Intent(this, UserPickupsActivity.class);
        intent.putExtra("userIdFromIntent",userIdFromIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new Notification.Builder(this, AllConstants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ResourcesCompat.getColor(getResources(),R.color.pastel_blue,null))
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(100,notification);

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        updateToken(token);

    }


    private void updateToken(String token){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            databaseReference.updateChildren(map);
        }




    }

    private void createNormalNotification(String title,String message,String userIdFromIntent,String chatId){

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AllConstants.CHANNEL_ID);
        builder.setContentTitle("New message: " + title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(getResources(),R.color.pastel_blue,null))
                .setSound(uri);

        Intent intent= new Intent(this, ChatsViewActivity.class);
        //intent.putExtra("chatId",chatId);
        //intent.putExtra("userIdFromIntent",userIdFromIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(85-65),builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOreoNotification(String title, String message, String userIdFromIntent, String chatId){

        NotificationChannel channel= new NotificationChannel(AllConstants.CHANNEL_ID,"Message",NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Message Description");
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Intent intent = new Intent(this, ChatsViewActivity.class);
        intent.putExtra("userIdFromIntent",userIdFromIntent);
        //intent.putExtra("chatId",chatId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new Notification.Builder(this, AllConstants.CHANNEL_ID)
                .setContentTitle("New message: " + title)
                .setContentText(message)
                .setColor(ResourcesCompat.getColor(getResources(),R.color.pastel_blue,null))
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(100,notification);
    }



}
