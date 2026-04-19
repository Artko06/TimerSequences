package com.example.timer.presentation.screen.list_seq_screen.screen

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.presentation.screen.list_seq_screen.viewModel.ListSequencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSequencesScreen(
    listSequencesViewModel: ListSequencesViewModel,
    onNavigateTimerSequence: (Long) -> Unit,
    onNavigateEditSequence: (Long) -> Unit,
    onNavigateSettings: () -> Unit
) {
    val localizedContext = LocalizedContext.current
    val state by listSequencesViewModel.state.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){}

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = localizedContext.getString(R.string.timer_title)
                ) },
                actions = {
                    IconButton(onClick = onNavigateSettings) {
                        Icon(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = localizedContext.getString(R.string.setting_title)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateEditSequence(-1L) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_sequence),
                    contentDescription = localizedContext.getString(R.string.adding_title)
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = state.listSequences,
                key = { it.id })
            { sequence ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onNavigateTimerSequence(sequence.id) },
                    colors = CardDefaults.cardColors(containerColor = Color(sequence.color).copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, Color(sequence.color))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sequence.seqName,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        IconButton(onClick = { onNavigateEditSequence(sequence.id) }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = localizedContext.getString(R.string.editing_title)
                            )
                        }


                    }
                }
            }
        }
    }
}