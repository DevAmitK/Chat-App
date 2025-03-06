package com.example.mychatapp.domain

import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object Constants {
    object OnlineTSUpdater{
        val UPDATE_IN_INTERVAL = 8L.seconds.inWholeMilliseconds
        val EXPIRE_STATUS_INTERVAL = 10L.seconds.inWholeMilliseconds
    }
}