package com.albertomrmekko.todolist.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmScheduler(
    private val context: Context
) {
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE)
                as AlarmManager

    fun scheduleTestAlarm() {
        val intent = Intent(
            context,
            TaskReminderReceiver::class.java
        ).apply {
            putExtra("task_title", "Test task")
            putExtra("notification_id", 12345)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            12345,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Log.d("ALARM", "Programando alarma de prueba")

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 10_000,
            pendingIntent
        )
    }
}