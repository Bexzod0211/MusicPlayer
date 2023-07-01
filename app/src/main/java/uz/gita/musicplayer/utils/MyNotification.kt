package uz.gita.musicplayer.utils

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cafe.adriel.voyager.androidx.AndroidScreen
import uz.gita.musicplayer.MainActivity
import uz.gita.musicplayer.R
import uz.gita.musicplayer.data.model.ThemeUtil
import uz.gita.musicplayer.presentation.ui.theme.MusicPlayerTheme

class MyNotification : AndroidScreen() {
    private val CHANNEL_ID = "Notify"



    @Composable
    override fun Content() {
        MyNotificationContent()
    }

    @Composable
    fun MyNotificationContent() {
        val context = LocalContext.current as Activity
        MusicPlayerTheme(darkTheme = ThemeUtil.value.value) {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = {
                        createChannel(context)
                        createNotification(context,title = "Music ", text = "My first notification")
                    }, modifier = Modifier.size(200.dp,60.dp)) {
                        Text(text = "Create notification")
                    }
                }
            }
        }
    }

    private fun createChannel(context: Context){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,"Notification",importance)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun createNotification(context:Context,title:String,text:String){

        val intent = Intent(context,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_IMMUTABLE)

        val broadcastIntent = Intent(context,MyBroadcastReceiver::class.java)

        val pendingBroadcastIntent = PendingIntent.getBroadcast(context,0,broadcastIntent,PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_music)
            .setContentTitle(title)
            .setContentText(text)
            .setContentInfo("Something else to content info")
            .setStyle(NotificationCompat.BigTextStyle())
            .addAction(R.drawable.icon_music,"Open",pendingIntent)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.icon_music,"Open broadcast",pendingBroadcastIntent)



        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        NotificationManagerCompat.from(context).notify(1,notificationBuilder.build())
    }


    @Preview
    @Composable
    fun MyNotificationPreview() {
        MyNotificationContent()
    }

}



