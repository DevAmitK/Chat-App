package com.example.mychatapp.ui.comp

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage


@Composable
fun AsyncImages(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    uri : String,
) {
    SubcomposeAsyncImage(
        model = uri,
        modifier = modifier
            .run {
                onClick?.let { clickable {   it() }} ?: this
            },
        contentScale = ContentScale.FillBounds,
        loading = {ImageLoading()},
        contentDescription =null
    )
}