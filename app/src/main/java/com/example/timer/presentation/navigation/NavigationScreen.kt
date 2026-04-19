package com.example.timer.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timer.presentation.screen.edit_seq_screen.screen.EditSequenceScreen
import com.example.timer.presentation.screen.edit_seq_screen.viewModel.EditSequenceViewModel
import com.example.timer.presentation.screen.list_seq_screen.screen.ListSequencesScreen
import com.example.timer.presentation.screen.list_seq_screen.viewModel.ListSequencesViewModel
import com.example.timer.presentation.screen.settings_screen.screen.SettingsScreen
import com.example.timer.presentation.screen.settings_screen.viewModel.SettingsViewModel
import com.example.timer.presentation.screen.timer_seq_screen.screen.TimerSequenceScreen
import com.example.timer.presentation.screen.timer_seq_screen.viewModel.TimerSequenceViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.ListSequences,
        enterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
    ) {
        composable<Route.ListSequences> {
            ListSequencesScreen(
                listSequencesViewModel = koinViewModel<ListSequencesViewModel>(),
                onNavigateTimerSequence = { sequenceId ->
                    navController.navigate(
                        route = Route.TimerSequence(sequenceId)
                    )
                },
                onNavigateEditSequence = { sequenceId ->
                    navController.navigate(
                        route = Route.EditSequence(sequenceId)
                    )
                },
                onNavigateSettings = {
                    navController.navigate(
                        route = Route.Settings
                    )
                }
            )
        }

        composable<Route.Settings> {
            SettingsScreen(
                settingsViewModel = koinViewModel<SettingsViewModel>(),
                onBackFromSettings = {
                    navController.navigateUp()
                }
            )
        }

        composable<Route.EditSequence> {
            EditSequenceScreen(
                editSequenceViewModel = koinViewModel<EditSequenceViewModel>(),
                onBackFromEditScreen = {
                    navController.navigateUp()
                }
            )
        }
        
        composable<Route.TimerSequence> {
            TimerSequenceScreen(
                timerSequenceViewModel = koinViewModel<TimerSequenceViewModel>(),
                onBackFromTimerSequence = {
                    navController.navigateUp()
                }
            ) 
        }
    }
}