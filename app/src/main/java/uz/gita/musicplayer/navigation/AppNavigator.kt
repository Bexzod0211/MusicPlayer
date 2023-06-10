package uz.gita.musicplayer.navigation

import cafe.adriel.voyager.androidx.AndroidScreen

typealias AppScreen = AndroidScreen

interface AppNavigator {
    suspend fun pop()
    suspend fun navigateTo(screen: AndroidScreen)
    suspend fun replace(screen: AndroidScreen)
}


