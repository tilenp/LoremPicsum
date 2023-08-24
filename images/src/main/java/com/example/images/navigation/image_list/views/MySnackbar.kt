package com.example.images.navigation.image_list.views

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MySnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String,
    onActionPerformed: () -> Unit
) {
    val scope = rememberCoroutineScope()
    SideEffect {
        scope.launch {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Indefinite
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                onActionPerformed()
            }
        }
    }
}