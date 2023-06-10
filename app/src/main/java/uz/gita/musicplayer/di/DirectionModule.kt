package uz.gita.musicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.musicplayer.presentation.ui.contract.MainContract
import uz.gita.musicplayer.presentation.ui.direction.MainDirection

@Module
@InstallIn(SingletonComponent::class)
interface DirectionModule {

    @Binds
    fun bindMainDirection(impl:MainDirection):MainContract.Direction
}