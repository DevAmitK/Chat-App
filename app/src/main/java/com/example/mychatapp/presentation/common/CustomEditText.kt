package com.example.mychatapp.presentation.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomEditText(
    modifier: Modifier = Modifier,
    value: String,
    label: String? = null,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    maxLines: Int = 100,
    readOnly: Boolean = false,
    enabled :Boolean = true
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        label = { if (label != null) Text(text = "$label")},
        shape = RoundedCornerShape(16.dp),
        isError = isError,
        supportingText = { if (isError) Text(text = "$errorMessage") },
        maxLines = maxLines,
        readOnly = readOnly,
        enabled = enabled,
    )
}