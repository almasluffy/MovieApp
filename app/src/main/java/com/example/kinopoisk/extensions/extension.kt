package com.example.kinopoisk.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.launchSafe(
    onError: (Throwable) -> Unit = {},
    onSuccess: suspend () -> Unit
) {
    launch {
        try {
            onSuccess()
        } catch (e: Throwable) {
            onError(e)
        }
    }
}