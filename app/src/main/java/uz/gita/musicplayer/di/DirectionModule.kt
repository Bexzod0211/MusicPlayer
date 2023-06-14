package uz.gita.musicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.musicplayer.presentation.ui.contract.MainContract
import uz.gita.musicplayer.presentation.ui.contract.PlayContract
import uz.gita.musicplayer.presentation.ui.direction.MainDirection
import uz.gita.musicplayer.presentation.ui.direction.PlayDirection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DirectionModule {

    @Binds
    fun bindMainDirection(impl:MainDirection):MainContract.Direction

    @[Binds Singleton]
    fun bindPlayDirection(impl:PlayDirection):PlayContract.Direction
}