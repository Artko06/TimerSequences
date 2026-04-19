package com.example.timer

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.timer.domain.mappers.toLocale
import com.example.timer.domain.model.ThemeApp
import com.example.timer.presentation.navigation.NavigationScreen
import com.example.timer.ui.theme.TimerTheme
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

val LocalizedContext = compositionLocalOf<Context> { error("Context not provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mainActivityViewModel = koinViewModel<MainActivityViewModel>()
            val theme = mainActivityViewModel.themeType.collectAsStateWithLifecycle().value
            val language = mainActivityViewModel.languageType.collectAsStateWithLifecycle().value

            val isDarkTheme = when(theme) {
                ThemeApp.SYSTEM -> isSystemInDarkTheme()
                ThemeApp.LIGHT -> false
                ThemeApp.DARK -> true
            }

            LaunchedEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT,
                        detectDarkMode = { isDarkTheme }
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT,
                        detectDarkMode = { isDarkTheme }
                    )
                )
            }

            val localizedContext = updateLocale(
                context = this,
                locale = language.toLocale()
            )

            CompositionLocalProvider(
                LocalizedContext provides localizedContext
            ) {
                TimerTheme(darkTheme = isDarkTheme) {
                    NavigationScreen()
                }
            }
        }
    }
}

fun updateLocale(context: Context, locale: Locale): Context {
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    return context.createConfigurationContext(config)
}

