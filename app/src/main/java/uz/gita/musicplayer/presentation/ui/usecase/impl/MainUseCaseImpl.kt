package uz.gita.musicplayer.presentation.ui.usecase.impl

import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer.domain.repository.AppRepository
import uz.gita.musicplayer.presentation.ui.usecase.MainUseCase
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(
    private val repository: AppRepository
): MainUseCase {
    override fun getAllTabs(): Flow<List<Int>> {
        return repository.getTabs()
    }


}