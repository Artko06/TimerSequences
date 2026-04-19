package com.example.timer.presentation.service.manager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.timer.domain.manager.TimerManager
import com.example.timer.presentation.service.TimerService
import com.example.timer.presentation.service.config.ACTION_STOP_SERVICE
import com.example.timer.presentation.service.config.ACTION_TOGGLE_RESUME_PAUSE
import com.example.timer.presentation.service.config.EXTRA_SEQUENCE_ID
import com.example.timer.presentation.service.config.SOUND_VOLUME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerManagerImpl(
    private val context: Context
) : TimerManager {
    private val _binder = MutableStateFlow<TimerService.TimerBinder?>(null)
    override val binder = _binder.asStateFlow()

    private var connection: ServiceConnection? = null

    override fun start(sequenceId: Long) {
        val intent = Intent(
            context,
            TimerService::class.java
        ).apply {
            putExtra(EXTRA_SEQUENCE_ID, sequenceId)
        }

        context.startForegroundService(intent)
    }

    override fun toggleResumePause() {
        val intent = Intent(context, TimerService::class.java).apply {
            action = ACTION_TOGGLE_RESUME_PAUSE
        }
        context.startForegroundService(intent)
    }

    override fun moveToNext() {
        _binder.value?.nextPhase()
    }

    override fun moveToPrevious() {
        _binder.value?.previousPhase()
    }

    override fun stop() {
        if (_binder.value == null) {
            unbind()
            return
        }

        val intent = Intent(context, TimerService::class.java).apply {
            action = ACTION_STOP_SERVICE
        }

        context.startService(intent)

        unbind()
    }

    override fun bind() {
        if (connection != null) return

        connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                _binder.value = service as TimerService.TimerBinder
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                _binder.value = null
                connection = null
            }
        }

        val intent = Intent(context, TimerService::class.java)
        context.bindService(intent, connection!!, Context.BIND_AUTO_CREATE)
    }

    override fun unbind() {
        connection?.let {
            context.unbindService(it)
            _binder.value = null
            connection = null
        }
    }
}