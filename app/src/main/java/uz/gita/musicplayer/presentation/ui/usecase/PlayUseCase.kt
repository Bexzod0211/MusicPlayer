package uz.gita.musicplayer.presentation.ui.usecase

import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.source.local.entity.MusicEntity

interface PlayUseCase {
    fun addMusic(music:MusicEntity):Flow<Unit>

    fun deleteMusic(music: MusicEntity):Flow<Unit>
    fun getAllFavouriteMusics():List<MusicData>
    fun deleteMusicByMusicId(musicId:Long):Flow<Unit>
}