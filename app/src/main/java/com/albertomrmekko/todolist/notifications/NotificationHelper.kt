package com.albertomrmekko.todolist.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationHelper {
    const val NOTIFICATION_CHANNEL_ID = "task_reminders"
    const val REBOOT_CHANNEL_ID = "alarm_reschedule"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Task reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for task reminders"
                enableVibration(true)
            }

            val rebootChannel = NotificationChannel(
                REBOOT_CHANNEL_ID,
                "Alarm reschedule",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for alarm scheduling"
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.createNotificationChannel(rebootChannel)
        }
    }
}