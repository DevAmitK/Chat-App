package com.example.mychatapp.ui.comp

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage


@Composable
fun ImagePicker(
    defaultIconResId: Int, // Pass the default drawable resource ID
    onImageUploadClick: () -> Unit, // Callback to handle image upload clicks
    imageUri: ImageState?
) {
    Log.d("ImagePicker", "ImagePicker: $imageUri")
    if (imageUri?.data() != null) {
        AsyncImage(
            modifier = Modifier
                .clickable { onImageUploadClick() }
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Inside,
            model = imageUri.data(), contentDescription = null)
    } else {
        Icon(
            painter = painterResource(id = defaultIconResId),
            contentDescription = "Upload Profile Image",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { onImageUploadClick() },
            tint = Color.Gray
        )
    }
}
