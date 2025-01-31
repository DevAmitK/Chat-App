package com.example.mychatapp.ui.comp

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun CustomSnackBar(
    message: String,
    actionLabel: String? = null,
    onActionClicked: (() -> Unit)? = null,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // SnackbarHost to display Snackbars
    SnackbarHost(hostState = snackbarHostState)

    // Trigger the Snackbar
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            )
            if (result == SnackbarResult.ActionPerformed) {
                onActionClicked?.invoke()
            }
        }
    }
}