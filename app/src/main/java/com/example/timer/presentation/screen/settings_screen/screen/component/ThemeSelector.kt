package com.example.timer.presentation.screen.settings_screen.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.domain.model.ThemeApp

@Composable
fun ThemeSelector(
    currentTheme: ThemeApp,
    onThemeSelected: (ThemeApp) -> Unit
) {
    val localizedContext = LocalizedContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = localizedContext.getString(R.string.theme_selection),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        ThemeApp.entries.forEach { theme ->
            val isSelected = theme == currentTheme

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = { onThemeSelected(theme) }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onThemeSelected(theme) }
                )

                Text(
                    text = when (theme) {
                        ThemeApp.SYSTEM -> localizedContext.getString(R.string.theme_system)
                        ThemeApp.LIGHT -> localizedContext.getString(R.string.theme_light)
                        ThemeApp.DARK -> localizedContext.getString(R.string.theme_dark)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}