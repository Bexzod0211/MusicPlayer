package uz.gita.musicplayer.presentation.ui.usecase.impl

import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.source.local.entity.MusicEntity
import uz.gita.musicplayer.domain.repository.AppRepository
import uz.gita.musicplayer.presentation.ui.usecase.PlayUseCase
import javax.inject.Inject

class PlayUseCaseImpl @Inject constructor(
    private val repository: AppRepository
):PlayUseCase{
    override fun addMusic(music: MusicEntity): Flow<Unit> {
        return repository.addMusic(music)
    }

    override fun deleteMusic(music: MusicEntity): Flow<Unit> {
        return repository.deleteMusic(music)
    }

    override fun getAllFavouriteMusics(): List<MusicData> {
        return repository.getAllFavouriteMusics()
    }

    override fun deleteMusicByMusicId(musicId: Long): Flow<Unit> {
        return repository.deleteMusicByMusicId(musicId)
    }
}