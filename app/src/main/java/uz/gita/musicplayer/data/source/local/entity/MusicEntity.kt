package uz.gita.musicplayer.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.musicplayer.data.model.MusicData

@Entity(tableName = "musics")
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val musicId:Long,
    val name:String,
    val artist:String,
    val albumId:Long,
    val data:String?,
    val duration:Long,
    val isFavourite:Boolean
){
    fun toData():MusicData = MusicData(musicId, name, artist, albumId, data, duration, isFavourite)
}
