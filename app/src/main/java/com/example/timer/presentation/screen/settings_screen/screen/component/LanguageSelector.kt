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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.domain.model.LanguageApp

@Composable
fun LanguageSelector(
    currentLanguage: LanguageApp,
    onLanguageSelected: (LanguageApp) -> Unit,
) {
    val localizedContext = LocalizedContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = localizedContext.getString(R.string.language_selection),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LanguageApp.entries.forEach { language ->
            val isSelected = language == currentLanguage

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = { onLanguageSelected(language) }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onLanguageSelected(language) }
                )

                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text(
                        text = when (language) {
                            LanguageApp.SYSTEM -> localizedContext.getString(R.string.language_system)
                            LanguageApp.ENGLISH -> "English"
                            LanguageApp.RUSSIAN -> "Русский"
                            LanguageApp.BELARUSIAN -> "Беларуская"
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}