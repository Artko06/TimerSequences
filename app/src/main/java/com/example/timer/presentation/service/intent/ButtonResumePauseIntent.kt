package com.example.timer.presentation.service.intent

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.timer.presentation.service.TimerService
import com.example.timer.presentation.service.config.ACTION_TOGGLE_RESUME_PAUSE
import com.example.timer.presentation.service.config.REQUEST_CODE_RESUME_PAUSE_SERVICE_ID

private fun buttonResumePauseIntent(context: Context) = Intent(
    context,
    TimerService::class.java
).apply { action = ACTION_TOGGLE_RESUME_PAUSE }

fun pendingButtonResumePauseIntent(context: Context): PendingIntent = PendingIntent.getService(
    context,
    REQUEST_CODE_RESUME_PAUSE_SERVICE_ID,
    buttonResumePauseIntent(context),
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)