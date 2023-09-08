package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFY_ID = 0

fun NotificationManager.sendNotification(
    applicationContext: Context,
    file: String,
    status: String,
    channelId: String,
    message: String
) {

    val detailActivityIntent = Intent(applicationContext, DetailActivity::class.java)
    detailActivityIntent.putExtra(DETAIL_ACTIVITY_STATUS_KEY, status)
    detailActivityIntent.putExtra(DETAIL_ACTIVITY_FILE_KEY, file)

    val buttonPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFY_ID,
        detailActivityIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(message).setAutoCancel(true).addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            buttonPendingIntent
        ).setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFY_ID, builder.build())
}