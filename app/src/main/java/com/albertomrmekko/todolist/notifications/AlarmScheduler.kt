package com.albertomrmekko.todolist.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE)
                as AlarmManager

    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleTaskAlarm(taskId: Long, taskTitle: String, triggerAtMillis: Long) {
        if (triggerAtMillis <= System.currentTimeMillis()) {
            Log.d(
                "ALARM",
                "La fecha de la alarma es anterior a la fecha actual."
            )
            return
        }

        val intent = Intent(
            context,
            TaskReminderReceiver::class.java
        ).apply {
            putExtra("task_title", taskTitle)
            putExtra("notification_id", taskId.toInt())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Log.d(
            "ALARM",
            "Programando alarma para '$taskTitle' en $triggerAtMillis"
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    fun cancelTaskAlarm(taskId: Long) {
        val intent = Intent(
            context,
            TaskReminderReceiver::class.java
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)

        Log.d(
            "ALARM",
            "Alarma cancelada para taskId=$taskId"
        )
    }
}