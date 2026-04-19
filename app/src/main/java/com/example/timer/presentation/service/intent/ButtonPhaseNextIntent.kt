package com.example.timer.presentation.service.intent

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.timer.presentation.service.TimerService
import com.example.timer.presentation.service.config.ACTION_PHASE_NEXT
import com.example.timer.presentation.service.config.REQUEST_CODE_PHASE_NEXT_SERVICE_ID

private fun buttonPhaseNextIntent(context: Context) = Intent(
    context,
    TimerService::class.java
).apply { action = ACTION_PHASE_NEXT }

fun pendingButtonPhaseNextIntent(context: Context): PendingIntent = PendingIntent.getService(
    context,
    REQUEST_CODE_PHASE_NEXT_SERVICE_ID,
    buttonPhaseNextIntent(context),
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)