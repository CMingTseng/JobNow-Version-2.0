package com.jobnow.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.jobnow.acitvity.MenuActivity;
import com.jobnow.acitvity.NotificationActivity;
import com.jobnow.acitvity.NotificationManagerActivity;
import com.jobnow.config.Config;
import com.jobnow.controller.NotificationController;
import com.jobnow.fragment.InterviewJobSeekerFragment;
import com.jobnow.fragment.JobMainFragment;
import com.jobnow.fragment.MainFragment;
import com.jobnow.models.NotificationRequest;
import com.jobnow.models.UserModel;
import com.newtech.jobnow.R;


public class FireBaseService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        int key = Integer.parseInt(remoteMessage.getNotification().getSound());
        sendNotification(remoteMessage.getNotification().getBody(),key);

    }

    private void sendNotification(String messageBody, int key) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
            String profile=sharedPreferences.getString(Config.KEY_USER_PROFILE,"");
            Gson gson= new Gson();
            UserModel userModel=gson.fromJson(profile,UserModel.class);
            int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
            if(userModel!=null) {
                CountNotificationAsystask countNotificationAsystask = new CountNotificationAsystask(1,new NotificationRequest(0, userModel.id));
                countNotificationAsystask.execute();
            }
            if(userId!=0){
                CountNotificationAsystask countNotificationAsystask = new CountNotificationAsystask(2,new NotificationRequest(userId,0));
                countNotificationAsystask.execute();
            }
        }catch (Exception ertr){

        }
        Intent intent;
        if(key==1){
            intent = new Intent(this, NotificationActivity.class);
        }else {
            intent = new Intent(this, NotificationManagerActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap thumb = Bitmap.createBitmap(120, 140, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(thumb);
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(0, 0, thumb.getWidth(), thumb.getHeight()), null);
        Drawable drawable = new BitmapDrawable(getResources(), thumb);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(((BitmapDrawable) drawable).getBitmap())
                .setContentTitle("JobNow Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    class CountNotificationAsystask extends AsyncTask<Void, Void, Integer> {

        NotificationRequest notificationRequest;
        int type;

        public CountNotificationAsystask(int type,NotificationRequest notificationRequest) {
            this.notificationRequest = notificationRequest;
            this.type=type;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                NotificationController controller = new NotificationController();
                return controller.CountNotification(notificationRequest);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer code) {
            try {
                if(code==0){
                    if(type==1) {
                        MenuActivity.txtCount.setVisibility(View.GONE);
                    }else {
                        MainFragment.txtCount.setVisibility(View.GONE);
                        JobMainFragment.txtCount.setVisibility(View.GONE);
                        InterviewJobSeekerFragment.txtCount.setVisibility(View.GONE);
                    }
                }else {
                    if (type==1) {
                        MenuActivity.txtCount.setVisibility(View.VISIBLE);
                        MenuActivity.txtCount.setText(code + "");
                    }else {
                        MainFragment.txtCount.setVisibility(View.VISIBLE);
                        JobMainFragment.txtCount.setVisibility(View.VISIBLE);
                        InterviewJobSeekerFragment.txtCount.setVisibility(View.VISIBLE);

                        MainFragment.txtCount.setText(code + "");
                        JobMainFragment.txtCount.setText(code + "");
                        InterviewJobSeekerFragment.txtCount.setText(code + "");
                    }
                }

            } catch (Exception e) {
            }
        }
    }
}
