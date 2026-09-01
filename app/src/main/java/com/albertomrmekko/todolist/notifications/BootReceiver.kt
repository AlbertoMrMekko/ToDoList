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
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        Log.d("BOOT_RECEIVER", "Dispositivo reiniciado. Reprogramando alarmas.")

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tasks = taskRepository.getAllTasks()

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
                        }
                    }

            } finally {
                pendingResult.finish()
            }
        }
    }
}
