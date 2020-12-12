package com.example.novaposhtaapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        sendNotification(p0.notification?.body)
    }

    override fun onNewToken(p0: String) {
        sendNotification(p0)
    }
    private fun sendNotification(messageBody : String?){
        try {
            val intent = Intent(this, MainActivity::class.java)
                .apply { flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val name = "Firebase message"
                val descriptionText = "Firebase message channel"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("FirebaseMessaginService0001", name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }
            val notificationBuilder = NotificationCompat.Builder(this, "FirebaseMessaginService0001")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.mipmap.ic_launcher_round
                    )
                )
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)

            notificationManager.notify(0, notificationBuilder.build())
        } catch (e : Exception){
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}