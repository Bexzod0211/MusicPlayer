package uz.gita.musicplayer.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.musicplayer.R
import uz.gita.musicplayer.data.source.local.MySharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val sharedPref:MySharedPreferences
) : AppRepository {
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

}