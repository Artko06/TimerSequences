package com.example.timer.presentation.screen.settings_screen.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.timer.LocalizedContext
import com.example.timer.R
import com.example.timer.presentation.screen.settings_screen.action.SettingsAction
import com.example.timer.presentation.screen.settings_screen.screen.component.LanguageSelector
import com.example.timer.presentation.screen.settings_screen.screen.component.ThemeSelector
import com.example.timer.presentation.screen.settings_screen.viewModel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBackFromSettings: () -> Unit
) {
    val localizedContext = LocalizedContext.current
    val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = localizedContext.getString(R.string.setting_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackFromSettings) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            ThemeSelector(
                currentTheme = settingsState.selectedTheme,
                onThemeSelected = { theme ->
                    settingsViewModel.onAction(SettingsAction.ChangeTheme(theme))
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            LanguageSelector(
                currentLanguage = settingsState.selectedLanguage,
                onLanguageSelected = { language ->
                    settingsViewModel.onAction(SettingsAction.ChangeLanguage(language))
                }
            )

            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        settingsViewModel.onAction(SettingsAction.DeleteAllSequences)
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
                    Text(text = localizedContext.getString(R.string.delete_all))
                }
            }
        }
    }
}