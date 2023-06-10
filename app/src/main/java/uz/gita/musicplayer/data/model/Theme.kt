package uz.gita.musicplayer.data.model

import androidx.compose.runtime.mutableStateOf

enum class ThemeEnum {
    DARK,LIGHT
}

object ThemeUtil {
    val value = mutableStateOf(false)
}