package com.example.mychatapp.ui.comp

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomDialog(
    showDialog: Boolean,
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    dismissButtonText: String? = null,
    onDismiss: (() -> Unit)? = null
) {
    if (showDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                onDismiss?.invoke()  // Dismiss logic if needed
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                if (dismissButtonText != null && onDismiss != null) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = dismissButtonText)
                    }
                }
            }
        )
    }
}

