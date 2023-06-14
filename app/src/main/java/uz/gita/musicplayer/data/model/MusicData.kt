package uz.gita.musicplayer.data.model

import android.net.Uri

data class MusicData(
    val id:Long,
    val name:String,
    val artist:String,
    val albumId:Long = 0,
    val data:String?,
    val duration:Long,
    var isFavourite:Boolean = false
)
