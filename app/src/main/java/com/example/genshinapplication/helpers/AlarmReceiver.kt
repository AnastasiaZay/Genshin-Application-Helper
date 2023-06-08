package com.example.genshinapplication.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.LocaleData
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.activities.MainActivity
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
        val followingCharactersId = db.getFollowingCharacters()
        val followingBooksId = db.getFollowingBook(followingCharactersId)
        val lstFollowingDays = db.getFollowDaysInfo(followingBooksId)
        val today = LocalDate.now().dayOfWeek.name.lowercase()
        if( lstFollowingDays.isEmpty() ) return
        if (!lstFollowingDays.contains(today)) return
        val followingCharactersNames = db.getFollowingCharactersNames(today)
        var description = context!!.getString(R.string.book_notification_one)
        for (name in followingCharactersNames){
            description +=" $name,"

        }
        description.dropLast(1) //убираем лишнюю запятую
        createNotification(
            context = context!!,
            description = description
        )
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
            .setSmallIcon(R.drawable.not_ic)
            .setContentTitle(context.getString(R.string.book_notification_title)+" ")
            .setContentText(context!!.getString(R.string.book_notification_one))
            .setStyle(NotificationCompat.BigTextStyle().bigText(description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        manager.notify(NOTIFICATION_ID, builder.build())
    }
}