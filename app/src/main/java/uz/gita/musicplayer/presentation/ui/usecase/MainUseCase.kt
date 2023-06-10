package uz.gita.musicplayer.presentation.ui.usecase

import kotlinx.coroutines.flow.Flow

interface MainUseCase {
    fun getAllTabs():Flow<List<Int>>
}