package com.example.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings

class AlertDetails(val context: Context) : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
      val mediaPlayer: MediaPlayer= MediaPlayer.create(context,Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer.start()
    }

}
