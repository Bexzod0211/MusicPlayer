package uz.gita.musicplayer.navigation

import cafe.adriel.voyager.androidx.AndroidScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDispatcher @Inject constructor() : AppNavigator, NavigationHandler {
    override val navigatorState = MutableSharedFlow<NavigationArgs>()

    private suspend fun navigate(args: NavigationArgs) {
        navigatorState.emit(args)
    }

    override suspend fun pop() = navigate {
        pop()
    }
    override suspend fun navigateTo(screen: AndroidScreen) = navigate {
        push(screen)
    }
    override suspend fun replace(screen: AndroidScreen) = navigate {
        replace(screen)
    }
}

