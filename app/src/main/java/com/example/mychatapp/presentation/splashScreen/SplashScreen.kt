package com.example.mychatapp.presentation.splashScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.ui.theme.SplashScreenGradient

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: SplashViewModel,
) {
    val scale = remember { Animatable(1f) } // Start scale at 1f

    // Launch the animation effect
    LaunchedEffect(key1 = Unit) {
        viewModel.checkUserIsLoginOrNot(navHostController)
        scale.animateTo(
            targetValue = 0.3f, // Target scale (zoom-out)
            animationSpec = tween(
                durationMillis = 2000, // Duration for the zoom-out animation
                easing = FastOutSlowInEasing
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashScreenGradient),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(250.dp)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value
                ),
            painter = painterResource(id = R.drawable.baseline_textsms_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

