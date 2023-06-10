package uz.gita.musicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.musicplayer.navigation.AppNavigator
import uz.gita.musicplayer.navigation.NavigationDispatcher
import uz.gita.musicplayer.navigation.NavigationHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @[Binds Singleton]
    fun bindNavigationHandler(impl:NavigationDispatcher):NavigationHandler

    @[Binds Singleton]
    fun bindAppNavigator(impl:NavigationDispatcher):AppNavigator
}