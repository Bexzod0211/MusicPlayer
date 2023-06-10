package uz.gita.musicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.musicplayer.domain.repository.AppRepository
import uz.gita.musicplayer.domain.repository.AppRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindAppRepository(impl:AppRepositoryImpl):AppRepository
}