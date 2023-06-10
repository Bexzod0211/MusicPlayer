package uz.gita.musicplayer.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getLangSelectedState():Boolean
    fun setLangSelectedState(state:Boolean)
    fun getLanguage():String
    fun setLanguage(language:String)
    fun getTabs():Flow<List<Int>>
}