package uz.gita.musicplayer.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.musicplayer.data.source.local.dao.MusicDao
import uz.gita.musicplayer.data.source.local.entity.MusicEntity
import javax.inject.Inject

@Database(entities = [MusicEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getMusicDao():MusicDao

    companion object {
        private lateinit var instance:AppDatabase

        fun init(context:Context){
            instance = Room
                .databaseBuilder(context,AppDatabase::class.java,"musics.db")
                .allowMainThreadQueries()
                .build()
        }

        fun getInstance():AppDatabase{
            return instance
        }
    }

}