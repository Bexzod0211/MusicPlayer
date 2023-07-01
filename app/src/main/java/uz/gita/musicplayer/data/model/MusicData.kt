package uz.gita.musicplayer.data.model

import uz.gita.musicplayer.data.source.local.entity.MusicEntity

data class MusicData(
    val musicId:Long,
    val name:String,
    val artist:String,
    val albumId:Long = 0,
    val data:String?,
    val duration:Long,
    var isFavourite:Boolean = false
){
    fun toEntity():MusicEntity = MusicEntity(0,musicId, name, artist, albumId, data, duration, isFavourite)
}

