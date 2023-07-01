package uz.gita.musicplayer.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import uz.gita.musicplayer.data.source.local.AppDatabase

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}