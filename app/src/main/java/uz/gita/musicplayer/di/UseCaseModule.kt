package uz.gita.musicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.musicplayer.presentation.ui.usecase.MainUseCase
import uz.gita.musicplayer.presentation.ui.usecase.PlayUseCase
import uz.gita.musicplayer.presentation.ui.usecase.impl.MainUseCaseImpl
import uz.gita.musicplayer.presentation.ui.usecase.impl.PlayUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindMainUseCase(impl:MainUseCaseImpl):MainUseCase

    @Binds
    fun bindPlayUseCase(impl:PlayUseCaseImpl):PlayUseCase
}