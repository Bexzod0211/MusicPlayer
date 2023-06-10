package uz.gita.musicplayer.presentation.ui.usecase.impl

import uz.gita.musicplayer.domain.repository.AppRepository
import uz.gita.musicplayer.presentation.ui.usecase.PlayUseCase
import javax.inject.Inject

class PlayUseCaseImpl @Inject constructor(
    private val repository: AppRepository
):PlayUseCase{
}