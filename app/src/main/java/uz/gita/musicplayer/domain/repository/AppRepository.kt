package uz.gita.musicplayer.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.source.local.entity.MusicEntity

interface AppRepository {
    fun getLangSelectedState():Boolean
    fun setLangSelectedState(state:Boolean)
    fun getLanguage():String
    fun setLanguage(language:String)
    fun getTabs():Flow<List<Int>>
    fun getAllFavouriteMusicsFlow():Flow<List<MusicData>>
    fun addMusic(music:MusicEntity):Flow<Unit>
    fun deleteMusic(music: MusicEntity):Flow<Unit>
    fun deleteMusicByMusicId(musicId:Long):Flow<Unit>
    fun getAllFavouriteMusics():List<MusicData>
}