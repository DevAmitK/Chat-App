package com.example.mychatapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mychatapp.presentation.navigation.NavHostGraph
import com.example.mychatapp.ui.theme.MyChatAppTheme
import com.example.mychatapp.ui.theme.MyChatTheme
import com.streamliners.base.BaseActivity
import com.streamliners.base.uiEvent.UiEventDialogs


class MainActivity: BaseActivity(){

    override var buildType: String = BuildConfig.BUILD_TYPE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyChatAppTheme {

                UiEventDialogs()
                NavHostGraph()


            }
        }
    }
}

