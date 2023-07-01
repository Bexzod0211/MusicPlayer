package uz.gita.musicplayer.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.musicplayer.R
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.source.local.AppDatabase
import uz.gita.musicplayer.data.source.local.MySharedPreferences
import uz.gita.musicplayer.data.source.local.entity.MusicEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val sharedPref:MySharedPreferences,
) : AppRepository {
    private val db = AppDatabase.getInstance()
    private val dao = db.getMusicDao()
    override fun getLangSelectedState(): Boolean {
        return sharedPref.isSelectLanguage
    }

    override fun setLangSelectedState(state: Boolean) {
        sharedPref.isSelectLanguage = state
    }

    override fun getLanguage(): String {
        return sharedPref.language
    }

    override fun setLanguage(language: String) {
        sharedPref.language = language
    }

    override fun getTabs(): Flow<List<Int>> = flow{
        val list = listOf(
            R.string.txt_favourites,
            R.string.txt_playlists,
            R.string.txt_tracks,
            R.string.txt_albums,
            R.string.txt_artists,
            R.string.txt_folders,
        )
        emit(list)
    }

    override fun getAllFavouriteMusicsFlow(): Flow<List<MusicData>>  = flow{
        emit(dao.getAllFavouritesMusics())
    }

    override fun addMusic(music: MusicEntity): Flow<Unit>  = flow{
        dao.addMusic(music)
        emit(Unit)
    }

    override fun deleteMusic(music: MusicEntity): Flow<Unit> = flow{
        dao.deleteMusic(music)
        emit(Unit)
    }

    override fun deleteMusicByMusicId(musicId: Long): Flow<Unit> = flow{
        dao.deleteMusicByMusicId(musicId)
        emit(Unit)
    }

    override fun getAllFavouriteMusics(): List<MusicData> {
        return dao.getAllFavouritesMusics()
    }

}