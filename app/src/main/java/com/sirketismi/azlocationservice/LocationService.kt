package com.sirketismi.azlocationservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val locationClient : GoogleLocationClient by lazy {
        GoogleLocationClient(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun start() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel_id"

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Konumlar güncelleniyor.")
            .setContentText("")
            .setSmallIcon(R.mipmap.ic_launcher)

        locationClient.getLocationUpdates(100)
            .catch {  }
            .onEach {
                val updatedNotification = notification
                    .setContentText("lat ${it.latitude} lat ${it.longitude}")
                notificationManager.notify(1, updatedNotification.build())
            }.launchIn(serviceScope)

        startForeground(1, notification.build())
    }
}