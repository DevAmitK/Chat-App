package com.example.mychatapp.ui.comp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mychatapp.ui.theme.grayTransparent

@Composable
fun LoadingCPI(modifier: Modifier) {
    Box(modifier = modifier.background(grayTransparent)) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }

}