package com.example.timer.presentation.service.intent

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.timer.presentation.service.TimerService
import com.example.timer.presentation.service.config.ACTION_PHASE_PREVIOUS
import com.example.timer.presentation.service.config.REQUEST_CODE_PHASE_PREVIOUS_SERVICE_ID

private fun buttonPhasePreviousIntent(context: Context) = Intent(
    context,
    TimerService::class.java
).apply { action = ACTION_PHASE_PREVIOUS }

fun pendingButtonPhasePreviousIntent(context: Context): PendingIntent = PendingIntent.getService(
    context,
    REQUEST_CODE_PHASE_PREVIOUS_SERVICE_ID,
    buttonPhasePreviousIntent(context),
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)