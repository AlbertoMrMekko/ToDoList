package com.albertomrmekko.todolist.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.albertomrmekko.todolist.data.repository.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {

        Log.d(
            "BOOT_RECEIVER",
            "Receiver ejecutado. Action=${intent.action}"
        )

        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        Log.d("BOOT_RECEIVER", "Dispositivo reiniciado. Reprogramando alarmas.")

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tasks = taskRepository.getAllTasks()

                Log.d(
                    "BOOT_RECEIVER", "Tareas encontradas: ${tasks.size}"
                )

                val now = System.currentTimeMillis()

                tasks
                    .filter { !it.completed }
                    .forEach { task ->

                        val date = task.date ?: return@forEach

                        val triggerAtMillis =
                            date
                                .atZone(ZoneId.of("Europe/Madrid"))
                                .toInstant()
                                .toEpochMilli()

                        if (triggerAtMillis > now) {
                            alarmScheduler.scheduleTaskAlarm(
                                taskId = task.id,
                                taskTitle = task.message,
                                triggerAtMillis = triggerAtMillis
                            )

                            Log.d(
                                "BOOT_RECEIVER",
                                "Alarma reprogramada para taskId=${task.id}"
                            )

                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

                            showRescheduledAlarmNotification(
                                context = context,
                                taskId = task.id,
                                taskTitle = task.message,
                                date = date.format(formatter)
                            )
                        } else {
                            Log.d(
                                "BOOT_RECEIVER",
                                "Task ${task.id} ignorada: la fecha ya ha pasado."
                            )
                        }
                    }
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun showRescheduledAlarmNotification(
        context: Context,
        taskId: Long,
        taskTitle: String,
        date: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as android.app.NotificationManager

        val notification = androidx.core.app.NotificationCompat.Builder(
            context,
            NotificationHelper.REBOOT_CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Alarma reprogramada")
            .setContentText("$taskTitle — $date")
            .setStyle(
                androidx.core.app.NotificationCompat.BigTextStyle()
                    .bigText(
                        "Tarea: $taskTitle\n" +
                                "Programada: $date\n" +
                                "Task ID: $taskId"
                    )
            )
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            taskId.toInt(),
            notification
        )
    }
}
