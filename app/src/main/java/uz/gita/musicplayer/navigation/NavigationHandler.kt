package uz.gita.musicplayer.navigation

import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.SharedFlow

typealias NavigationArgs = Navigator.() -> Unit

interface NavigationHandler {
    val navigatorState: SharedFlow<NavigationArgs>
}

