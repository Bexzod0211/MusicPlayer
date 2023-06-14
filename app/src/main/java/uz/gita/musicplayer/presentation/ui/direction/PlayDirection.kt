package uz.gita.musicplayer.presentation.ui.direction

import uz.gita.musicplayer.navigation.AppNavigator
import uz.gita.musicplayer.presentation.ui.contract.PlayContract
import javax.inject.Inject

class PlayDirection @Inject constructor(
    private val appNavigator: AppNavigator
) : PlayContract.Direction {
    override suspend fun backToMain() {
        appNavigator.pop()
    }

}