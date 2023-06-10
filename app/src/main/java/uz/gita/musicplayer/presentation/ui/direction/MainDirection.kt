package uz.gita.musicplayer.presentation.ui.direction

import uz.gita.musicplayer.navigation.AppNavigator
import uz.gita.musicplayer.presentation.ui.contract.MainContract
import uz.gita.musicplayer.presentation.ui.screens.play.PlayScreen
import javax.inject.Inject

class MainDirection @Inject constructor(
    private val appNavigator: AppNavigator
) :MainContract.Direction{
    override suspend fun openPlayScreen() {
        appNavigator.navigateTo(PlayScreen())
    }
}