package com.sciasv.asv.utils;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.sciasv.asv.R;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static android.support.v4.app.NotificationCompat.DEFAULT_SOUND;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;


/**
 * Created by Geek Nat on 5/9/2016.
 */
public class ResponseHandler {
    Context context;

    public ResponseHandler(Context context) {
        this.context = context;
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showNotification(Context context, String title, String message, PendingIntent pendingIntent, Boolean setOngoing, Boolean setAutoCancel, int notifcationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setOngoing(setOngoing);
        builder.setAutoCancel(setAutoCancel);
        builder.setContentTitle(title);
        builder.setContentText(message);
       // builder.setSmallIcon(R.drawable.ic_notty);
        builder.setTicker(message);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000});
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifcationId, builder.build());
    }

    public void showDialog(String title, String message) {
        final PrettyDialog pDialog = new PrettyDialog(context);
        pDialog.setCancelable(false);
        pDialog.setIcon(
                R.drawable.pdlg_icon_info,
                R.color.pdlg_color_green, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        pDialog.dismiss();
                    }
                })
                .setTitle(title)
                .setMessage(message)
                .addButton(
                        "OKAY",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_green,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                pDialog.dismiss();
                            }
                        }
                )
                .show();
    }


    public interface dialogListener {
        void onPositiveClick(Dialog dialog);

        void onNegativeClick(Dialog dialog);
    }

}
