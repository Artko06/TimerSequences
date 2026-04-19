package com.example.timer.presentation.screen.timer_seq_screen.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.domain.model.PhaseType
import com.example.timer.presentation.screen.timer_seq_screen.action.TimerSequenceAction
import com.example.timer.presentation.screen.timer_seq_screen.screen.component.PhaseItemRow
import com.example.timer.presentation.screen.timer_seq_screen.screen.component.getPhaseName
import com.example.timer.presentation.screen.timer_seq_screen.viewModel.TimerSequenceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerSequenceScreen(
    timerSequenceViewModel: TimerSequenceViewModel,
    onBackFromTimerSequence: () -> Unit
) {
    val localizedContext = LocalizedContext.current
    val state by timerSequenceViewModel.state.collectAsStateWithLifecycle()

    val allPhases = state.sequence?.phases ?: emptyList()
    val upcomingStartIndex = state.currentPhaseIndex + 1
    val upcomingPhases = allPhases.drop(upcomingStartIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.sequence?.seqName ?: localizedContext.getString(R.string.timer_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackFromTimerSequence) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 24.dp)
                ) {
                    Text(
                        text = allPhases.getOrNull(state.currentPhaseIndex)?.let {
                            getPhaseName(it.type)
                        } ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
                        CircularProgressIndicator(
                            progress = { if (state.currentAllSeconds > 0) state.timeLeftSeconds.toFloat() / state.currentAllSeconds else 0f },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 12.dp,
                            color = Color(state.sequence?.color ?: Color.Blue.toArgb()),
                        )
                        Text(
                            text = state.timeLeftSeconds.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(text = localizedContext.getString(R.string.cycle_2arg, state.currentRepeat, state.sequence?.repeatCount ?: 1))

                    Spacer(modifier = Modifier.height(28.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(
                                    onClick = { timerSequenceViewModel.onAction(TimerSequenceAction.MoveToPreviousPhase) }
                                ) {
                                    Text(
                                        text = localizedContext.getString(R.string.previous)
                                            .lowercase(),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }

                            FloatingActionButton(
                                onClick = { timerSequenceViewModel.onAction(TimerSequenceAction.ToggleTimer) },
                                modifier = Modifier.size(80.dp),
                                shape = CircleShape
                            ) {
                                Icon(
                                    painter = if (state.isRunning) {
                                        painterResource(R.drawable.ic_pause)
                                    } else {
                                        painterResource(R.drawable.ic_play)
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(
                                    onClick = { timerSequenceViewModel.onAction(TimerSequenceAction.MoveToNextPhase) }
                                ) {
                                    Text(
                                        text = localizedContext.getString(R.string.next)
                                            .lowercase(),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }

                        TextButton(
                            onClick = { timerSequenceViewModel.onAction(TimerSequenceAction.StopTimer) },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(
                                text = localizedContext.getString(R.string.stop).uppercase(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            if (upcomingPhases.isNotEmpty()) {
                item {
                    Text(
                        text = localizedContext.getString(R.string.upcoming_phases),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            items(upcomingPhases.size) { index ->
                val realIndex = index + upcomingStartIndex
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                {
                    PhaseItemRow(
                        index = realIndex,
                        phase = upcomingPhases[index]
                    )
                }
            }
        }
    }
}

