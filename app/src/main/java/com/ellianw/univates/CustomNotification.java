package com.ellianw.univates;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CustomNotification extends Worker {

    public Context context = null;
    private String CHANNEL_ID = "NotificationChannel";
    private String strItem = null;
    private String strQuantity = null;

    public CustomNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        executa();
        return Result.success();
    }

    public void executa() {
        strItem = getInputData().getString("name");
        strQuantity = getInputData().getString("qtt");
        String text = "Inserção do item "+ strItem +" realizada com "+ strQuantity +" unidades";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Inserção realizada!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Canal de leitura humana", NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);

            builder.setChannelId(CHANNEL_ID);
        }

        notificationManager.notify(0, builder.build());
    }
}