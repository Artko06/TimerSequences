package com.example.timer.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Binder
import android.os.IBinder
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.domain.model.PhaseItem
import com.example.timer.domain.model.PhaseType
import com.example.timer.domain.useCase.GetSequenceByIdUseCase
import com.example.timer.presentation.service.config.ACTION_PHASE_NEXT
import com.example.timer.presentation.service.config.ACTION_PHASE_PREVIOUS
import com.example.timer.presentation.service.config.ACTION_STOP_SERVICE
import com.example.timer.presentation.service.config.ACTION_TOGGLE_RESUME_PAUSE
import com.example.timer.presentation.service.config.DURATION_MS_SOUND
import com.example.timer.presentation.service.config.EXTRA_SEQUENCE_ID
import com.example.timer.presentation.service.config.NOTIFICATION_ID
import com.example.timer.presentation.service.config.SOUND_VOLUME
import com.example.timer.presentation.service.config.TIMER_CHANNEL_ID
import com.example.timer.presentation.service.config.TIMER_CHANNEL_NAME
import com.example.timer.presentation.service.intent.pendingButtonPhaseNextIntent
import com.example.timer.presentation.service.intent.pendingButtonPhasePreviousIntent
import com.example.timer.presentation.service.intent.pendingButtonResumePauseIntent
import com.example.timer.presentation.service.intent.pendingOpenActivityIntent
import com.example.timer.presentation.service.state.TimerServiceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TimerService: Service() {
    private var timerJob: Job? = null
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    private val _toneGen: ToneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, SOUND_VOLUME)

    private val getSequenceByIdUseCase: GetSequenceByIdUseCase by inject<GetSequenceByIdUseCase>()

    private val _state = MutableStateFlow(TimerServiceState())
    val state = _state.asStateFlow()

    private var phases: List<PhaseItem> = emptyList()
    private var totalRepeats: Int = 1

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_TOGGLE_RESUME_PAUSE -> {
                toggleStatus()
                return START_NOT_STICKY
            }

            ACTION_PHASE_PREVIOUS -> {
                previousPhase()
                return START_NOT_STICKY
            }

            ACTION_PHASE_NEXT -> {
                nextPhase()
                return START_NOT_STICKY
            }

            ACTION_STOP_SERVICE -> {
                finishSequence()
                return START_NOT_STICKY
            }
        }

        val sequenceId = intent?.getLongExtra(EXTRA_SEQUENCE_ID, -1L) ?: -1L

        if (sequenceId != -1L) {
            runNewSequence(sequenceId)
        } else {
            stopSelf()
        }

        return START_STICKY
    }

    private fun runNewSequence(sequenceId: Long) {
        timerJob?.cancel()

        timerJob = serviceScope.launch {
            val sequence = getSequenceByIdUseCase(sequenceId)
            if (sequence != null && sequence.phases.isNotEmpty()) {
                phases = sequence.phases
                totalRepeats = sequence.repeatCount

                _state.update { it.copy(
                    runningId = sequence.id,
                    currentColor = sequence.color,
                    isRunning = true,
                    isFinished = false
                ) }

                runSequenceCycle()
            } else {
                stopSelf()
            }
        }
    }

    private suspend fun runSequenceCycle() {
        while (_state.value.currentRepeat <= totalRepeats) {

            for (index in _state.value.currentPhaseIndex until phases.size) {
                val phase = phases[index]

                if (_state.value.currentPhaseIndex != index || _state.value.leftSeconds <= 0) {
                    runPhase(phase, index)
                }

                while (_state.value.leftSeconds > 0) {
                    if (_state.value.isRunning) {
                        delay(1000)
                        if (_state.value.isRunning) {
                            _state.update { it.copy(leftSeconds = it.leftSeconds - 1) }
                            updateNotification(_state.value.leftSeconds)
                            if (_state.value.leftSeconds == 0) playSound()
                        }
                    } else {
                        delay(100)
                    }
                }
            }
            if (_state.value.currentRepeat < totalRepeats) {
                _state.update { it.copy(currentRepeat = it.currentRepeat + 1, currentPhaseIndex = 0) }
            } else {
                break
            }
        }
        finishSequence()
    }

    private fun runPhase(phase: PhaseItem, indexPhase: Int) {
        val phaseName = getString(when (phase.type) {
            PhaseType.WORK -> R.string.phases_timer_work
            PhaseType.REST -> R.string.phases_timer_rest
            PhaseType.WARMUP -> R.string.phases_timer_warmup
            PhaseType.COOLDOWN -> R.string.phases_timer_cooldown
        })

        _state.update {
            it.copy(
                currentPhaseIndex = indexPhase,
                leftSeconds = phase.durationPhase,
                currentPhaseType = phaseName,
                allSeconds = phase.durationPhase
            )
        }

        val notification = createNotification(
            allSeconds = _state.value.allSeconds,
            leftSeconds = _state.value.leftSeconds,
            phaseType = phaseName,
            color = _state.value.currentColor
        )
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun finishSequence() {
        _state.update { it.copy(
            isFinished = true,
            isRunning = false,
            currentPhaseIndex = 0,
            currentRepeat = 1,
            leftSeconds = 0
        ) }

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder = TimerBinder()

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
        fun nextPhase() = this@TimerService.nextPhase()
        fun previousPhase() = this@TimerService.previousPhase()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJob?.cancel()
        serviceJob.cancel()
        _toneGen.release()
    }

    fun nextPhase() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            val nextIndex = _state.value.currentPhaseIndex + 1

            if (nextIndex == phases.size && _state.value.currentRepeat == totalRepeats) {
                finishSequence()
                return@launch
            }

            if (nextIndex < phases.size) {
                runPhase(phases[nextIndex], nextIndex)
                runSequenceCycle()
            } else if (_state.value.currentRepeat < totalRepeats) {
                val nextRepeat = _state.value.currentRepeat + 1
                _state.update { it.copy(currentRepeat = nextRepeat) }
                runPhase(phases[0], 0)
                runSequenceCycle()
            } else {
                finishSequence()
            }
        }
    }

    fun previousPhase() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            val currentIndex = _state.value.currentPhaseIndex

            if (currentIndex > 0) {
                val prevIndex = currentIndex - 1
                runPhase(phases[prevIndex], prevIndex)
                runSequenceCycle()
            } else if (_state.value.currentRepeat > 1) {
                val prevRepeat = _state.value.currentRepeat - 1
                val lastIndex = phases.size - 1
                _state.update { it.copy(currentRepeat = prevRepeat) }
                runPhase(phases[lastIndex], lastIndex)
                runSequenceCycle()
            } else {
                runPhase(phases[0], 0)
                runSequenceCycle()
            }
        }
    }

    private fun playSound() {
        _toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, DURATION_MS_SOUND)
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            TIMER_CHANNEL_ID,
            TIMER_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setSound(null, null)
            enableVibration(false)
            enableLights(false)
            setShowBadge(false)
        }

        getSystemService(NotificationManager::class.java).createNotificationChannel(
            notificationChannel
        )
    }

    private fun updateNotification(seconds: Int) {
        val notification = createNotification(
            allSeconds = _state.value.allSeconds,
            leftSeconds = seconds,
            phaseType = _state.value.currentPhaseType,
            color = _state.value.currentColor,
        )

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotification(leftSeconds: Int, allSeconds: Int, phaseType: String, color: Int): Notification {

        val actionResumePauseText = if (
            _state.value.isRunning
        ) getString(R.string.timer_service_pause)
        else getString(R.string.timer_service_resume)

        return Notification.Builder(this, TIMER_CHANNEL_ID)
            .setContentTitle(leftSeconds.toString())
            .setContentText(
                getString(
                    R.string.text_notification_timer,
                    phaseType
                )
            )
            .setContentIntent(pendingOpenActivityIntent(this))
            .addAction(
                Notification.Action.Builder(
                    null,
                    getString(R.string.previous),
                    pendingButtonPhasePreviousIntent(this)
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    null,
                    actionResumePauseText,
                    pendingButtonResumePauseIntent(this)
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    null,
                    getString(R.string.next),
                    pendingButtonPhaseNextIntent(this)
                ).build()
            )
            .setColor(color)
            .setSmallIcon(R.drawable.ic_timer)
            .setShowWhen(false)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setProgress(allSeconds, leftSeconds, false)
            .build()
    }

    private fun toggleStatus() {
        _state.update {
            it.copy(
                isRunning = !it.isRunning
            )
        }
        updateNotification(_state.value.leftSeconds)
    }
}