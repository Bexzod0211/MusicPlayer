package uz.gita.musicplayer.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    private val STORAGE = "MusicPlayer"
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context):SharedPreferences = context.getSharedPreferences(STORAGE,Context.MODE_PRIVATE)
}