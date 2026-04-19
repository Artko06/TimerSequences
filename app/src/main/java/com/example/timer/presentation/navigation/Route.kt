package com.example.timer.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object ListSequences: Route

    @Serializable
    data class TimerSequence(val sequenceId: Long): Route

    @Serializable
    data class EditSequence(val sequenceId: Long): Route

    @Serializable
    data object Settings: Route
}