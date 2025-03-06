package com.example.mychatapp.domain

import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object Constants {
    object OnlineTSUpdater{
        val UPDATE_IN_INTERVAL = 1L.minutes.inWholeMilliseconds
        val EXPIRE_STATUS_INTERVAL = 1L.minutes.inWholeMilliseconds
    }
}