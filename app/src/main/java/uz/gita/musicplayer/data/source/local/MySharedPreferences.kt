package uz.gita.musicplayer.data.source.local

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MySharedPreferences @Inject constructor(
    private val pref:SharedPreferences
) {
    private val STATE_LANGUAGE_SELECTED = "STATE_LANGUAGE_SELECTED"
    private val LANGUAGE = "LANGUAGE"

    var isSelectLanguage:Boolean
        get() = pref.getBoolean(STATE_LANGUAGE_SELECTED,false)
        set(value) = pref.edit().putBoolean(STATE_LANGUAGE_SELECTED,value).apply()

    var language:String
        get() = pref.getString(LANGUAGE,"").toString()
        set(value) = pref.edit().putString(LANGUAGE,value).apply()

}