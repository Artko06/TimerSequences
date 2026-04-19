package com.example.timer.presentation.service.intent

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.timer.MainActivity
import com.example.timer.presentation.service.config.REQUEST_CODE_OPEN_ACTIVITY_ID

private fun openActivityIntent(context: Context): Intent = Intent(context, MainActivity::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}

fun pendingOpenActivityIntent(context: Context): PendingIntent = PendingIntent.getActivity(
    context,
    REQUEST_CODE_OPEN_ACTIVITY_ID,
    openActivityIntent(context),
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)