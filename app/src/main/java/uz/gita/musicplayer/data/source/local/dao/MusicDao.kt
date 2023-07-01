package uz.gita.musicplayer.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.source.local.entity.MusicEntity

@Dao
interface MusicDao {
    @Insert
    fun addMusic(music:MusicEntity)

    @Update
    fun updateMusic(music: MusicEntity)

    @Delete
    fun deleteMusic(music: MusicEntity)

    @Query("SELECT * FROM musics")
    fun getAllFavouritesMusics():List<MusicData>

    @Query("DELETE FROM musics WHERE musicId = :musicId")
    fun deleteMusicByMusicId(musicId:Long)
}