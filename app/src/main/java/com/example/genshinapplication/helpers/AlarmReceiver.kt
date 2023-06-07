package com.example.plannerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.activities.MainActivity
import com.example.genshinapplication.helpers.MyDatabaseHelper
import java.time.LocalDate

const val NOTIFICATION_ID = 1
const val CHANNEL_NAME = "События"
const val CHANNEL_ID = "MY_CHANNEL_ID"

class AlarmReceiver: BroadcastReceiver()  {

    private lateinit var db: MyDatabaseHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        db = MyDatabaseHelper(context!!)
        handleAlarmData(context)
    }

    private fun handleAlarmData(context: Context?) {
////        val lstTomorrow = db.getNotesOfDay( LocalDate.now().plusDays(1).toEpochDay() )
//        if( lstTomorrow.isEmpty() ) return
//
//        var description = "У вас завтра"
//            description +=
//                if( lstTomorrow.size == 1) " 1 запланированное событие"
//                else " ${lstTomorrow.size} запланированных событий"
//
//        createNotification(
//            context = context!!,
//            description = description
//        )
    }

    private fun createNotification( context: Context, description : String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Канал для предстоящих планов"

        manager.createNotificationChannel(channel)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                123,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle("Грядут планы")
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        manager.notify(NOTIFICATION_ID, builder.build())
    }
}