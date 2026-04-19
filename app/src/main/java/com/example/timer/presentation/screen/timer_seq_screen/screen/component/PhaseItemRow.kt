package com.example.timer.presentation.screen.timer_seq_screen.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.domain.model.PhaseItem
import com.example.timer.domain.model.PhaseType

@Composable
fun PhaseItemRow(index: Int, phase: PhaseItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${index + 1}. " + getPhaseName(phase.type),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = phase.durationPhase.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun getPhaseName(type: PhaseType): String {
    val localizedContext = LocalizedContext.current

    return when (type) {
        PhaseType.WORK -> localizedContext.getString(R.string.phases_timer_work)
        PhaseType.REST -> localizedContext.getString(R.string.phases_timer_rest)
        PhaseType.WARMUP -> localizedContext.getString(R.string.phases_timer_warmup)
        PhaseType.COOLDOWN -> localizedContext.getString(R.string.phases_timer_cooldown)
    }
}