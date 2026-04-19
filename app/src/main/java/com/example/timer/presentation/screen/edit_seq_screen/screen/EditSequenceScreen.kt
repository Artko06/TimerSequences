package com.example.timer.presentation.screen.edit_seq_screen.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.presentation.screen.edit_seq_screen.action.EditSequenceAction
import com.example.timer.presentation.screen.edit_seq_screen.screen.component.ColorPickerRow
import com.example.timer.presentation.screen.edit_seq_screen.screen.component.CounterControl
import com.example.timer.presentation.screen.edit_seq_screen.screen.component.PhaseEditItem
import com.example.timer.presentation.screen.edit_seq_screen.viewModel.EditSequenceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSequenceScreen(
    editSequenceViewModel: EditSequenceViewModel,
    onBackFromEditScreen: () -> Unit
) {
    val state by editSequenceViewModel.state.collectAsStateWithLifecycle()
    val localizedContext = LocalizedContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.sequenceId == null) {
                            localizedContext.getString(R.string.creating_timer_title)
                        } else {
                            localizedContext.getString(R.string.editing_timer_title)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackFromEditScreen) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        editSequenceViewModel.onAction(EditSequenceAction.SaveSequence)
                        onBackFromEditScreen()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = state.seqName,
                    onValueChange = {
                        editSequenceViewModel.onAction(
                            EditSequenceAction.UpdateName(
                                it
                            )
                        )
                    },
                    label = { Text(text = localizedContext.getString(R.string.name_timer)) },
                    placeholder = { Text(text = localizedContext.getString(R.string.example_name_timer)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Text(
                    text = localizedContext.getString(R.string.colour),
                    style = MaterialTheme.typography.labelMedium
                )
                ColorPickerRow(
                    selectedColor = state.color,
                    onColorSelected = {
                        editSequenceViewModel.onAction(
                            EditSequenceAction.UpdateColor(
                                it
                            )
                        )
                    }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = localizedContext.getString(R.string.repetitions),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    CounterControl(
                        value = state.repeatCount,
                        onValueChange = {
                            editSequenceViewModel.onAction(
                                EditSequenceAction.UpdateRepeatCount(
                                    it
                                )
                            )
                        }
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text(
                    text = localizedContext.getString(R.string.phases),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            itemsIndexed(state.phases) { index, phase ->
                PhaseEditItem(
                    phase = phase,
                    onUpdate = { type, time ->
                        editSequenceViewModel.onAction(
                            EditSequenceAction.UpdatePhase(
                                index,
                                type,
                                time
                            )
                        )
                    },
                    onDelete = { editSequenceViewModel.onAction(EditSequenceAction.RemovePhase(index)) },
                    onMoveUp = { editSequenceViewModel.onAction(EditSequenceAction.MovePhaseUp(index)) },
                    onMoveDown = {
                        editSequenceViewModel.onAction(
                            EditSequenceAction.MovePhaseDown(
                                index
                            )
                        )
                    }
                )
            }

            item {
                OutlinedButton(
                    onClick = { editSequenceViewModel.onAction(EditSequenceAction.AddPhase(30)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = localizedContext.getString(R.string.add_phase))
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        editSequenceViewModel.onAction(EditSequenceAction.DeleteSequence)
                        onBackFromEditScreen()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                        containerColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))
                    Text(text = localizedContext.getString(R.string.delete_timer))
                }
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}