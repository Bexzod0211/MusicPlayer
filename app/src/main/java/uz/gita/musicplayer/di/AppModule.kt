package uz.gita.musicplayer.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.musicplayer.data.source.local.AppDatabase

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    private val STORAGE = "MusicPlayer"
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context):SharedPreferences = context.getSharedPreferences(STORAGE,Context.MODE_PRIVATE)

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context):AppDatabase = Room
        .databaseBuilder(context,AppDatabase::class.java,"musics.db")
        .allowMainThreadQueries()
        .build()
}