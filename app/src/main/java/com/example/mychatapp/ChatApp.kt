package com.example.mychatapp

import android.app.Application
import com.example.mychatapp.koin.appModule
import com.example.mychatapp.koin.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class ChatApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@ChatApp)
            modules(listOf(appModule , viewModel))
        }
    }
}