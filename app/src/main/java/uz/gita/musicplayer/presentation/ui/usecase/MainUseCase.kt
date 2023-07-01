package uz.gita.musicplayer.presentation.ui.usecase

import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer.data.model.MusicData

interface MainUseCase {
    fun getAllTabs():Flow<List<Int>>
    fun getAllFavouriteMusicsFlow():Flow<List<MusicData>>
    fun getAllFavouriteMusics():List<MusicData>
}