package com.dev.carrenting.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toInstant(): Instant {
    val zoneId = ZoneId.systemDefault() // Use the desired time zone

    return this.atZone(zoneId).toInstant()
}
