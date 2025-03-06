package com.example.mychatapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mychatapp.BuildConfig
import com.example.mychatapp.domain.Constants
import com.example.mychatapp.domain.local.repo.LocalRepo
import com.example.mychatapp.domain.usecase.LastOnlineTSUpdater
import com.example.mychatapp.presentation.navigation.NavHostGraph
import com.example.mychatapp.ui.theme.MyChatAppTheme
import com.streamliners.base.BaseActivity
import com.streamliners.base.uiEvent.UiEventDialogs
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity: BaseActivity(){

    override var buildType: String = BuildConfig.BUILD_TYPE
    internal val localRepo: LocalRepo by inject()
     internal val lastOnlineTSUpdater: LastOnlineTSUpdater by inject()

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

    internal lateinit var onlineTSUpdaterJob: Job

    override fun onPause() {
        super.onPause()
        if (::onlineTSUpdaterJob.isInitialized) {
            onlineTSUpdaterJob.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        initializeOnlineTSUpdate()
    }

}

