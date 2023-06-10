package uz.gita.musicplayer.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "onReceive in BroadCast", Toast.LENGTH_SHORT).show()
        myLog("onReceive in BroadCast")
    }
}