package uz.gita.musicplayer.utils

import android.database.Cursor
import kotlinx.coroutines.flow.MutableStateFlow
import uz.gita.musicplayer.utils.base.getMusicByPos

object MyEventBus {
    var cursor:Cursor? = null
    var selectedPos:Int = -1

    var currentTime:Int = 0
    var totalTime:Long = 0

    val currentTimeFlow = MutableStateFlow(0)

    var musicIsPlaying = MutableStateFlow(false)
    var selectedMusic = MutableStateFlow(cursor?.getMusicByPos(0))
    var isShuffle:Boolean = false

    var isRepeatOne:Boolean = false
}