package com.example.mychatapp.ui.main

import com.example.mychatapp.domain.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun MainActivity.initializeOnlineTSUpdate() {
    execute(false) {
        if (localRepo.getLoginState()){
            onlineTSUpdaterJob = launch {
                updateOnlineTS()
            }
        }
    }

}

suspend fun MainActivity.updateOnlineTS() {
    lastOnlineTSUpdater.updateTS()
    delay(Constants.OnlineTSUpdater.UPDATE_IN_INTERVAL)
    updateOnlineTS()
}