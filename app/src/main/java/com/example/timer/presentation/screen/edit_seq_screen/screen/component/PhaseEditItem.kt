package com.example.timer.presentation.screen.edit_seq_screen.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.domain.model.PhaseItem
import com.example.timer.domain.model.PhaseType

@Composable
fun PhaseEditItem(
    phase: PhaseItem,
    onUpdate: (PhaseType, Int) -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                IconButton(onClick = onMoveUp, modifier = Modifier.size(32.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit_arrow_up),
                        contentDescription = null
                    )
                }
                IconButton(onClick = onMoveDown, modifier = Modifier.size(32.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit_arrow_down),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                PhaseTypeSelector(
                    currentType = phase.type,
                    onTypeChange = { newType -> onUpdate(newType, phase.durationPhase) }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (phase.durationPhase > 5) onUpdate(phase.type, phase.durationPhase - 5)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_remove),
                            contentDescription = null
                        )
                    }

                    Text(
                        text = phase.durationPhase.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(onClick = {
                        onUpdate(phase.type, phase.durationPhase + 5)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun PhaseTypeSelector(
    currentType: PhaseType,
    onTypeChange: (PhaseType) -> Unit
) {
    val localizedContext = LocalizedContext.current
    val nextType = when (currentType) {
        PhaseType.WARMUP -> PhaseType.WORK
        PhaseType.WORK -> PhaseType.REST
        PhaseType.REST -> PhaseType.COOLDOWN
        PhaseType.COOLDOWN -> PhaseType.WARMUP
    }

    TextButton(onClick = { onTypeChange(nextType) }) {
        val label = when (currentType) {
            PhaseType.WORK -> localizedContext.getString(R.string.phases_timer_work)
            PhaseType.REST -> localizedContext.getString(R.string.phases_timer_rest)
            PhaseType.WARMUP -> localizedContext.getString(R.string.phases_timer_warmup)
            PhaseType.COOLDOWN -> localizedContext.getString(R.string.phases_timer_cooldown)
        }

        Text(text = label, style = MaterialTheme.typography.labelLarge)
    }
}