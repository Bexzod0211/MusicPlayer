package uz.gita.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.musicplayer.data.model.ThemeUtil
import uz.gita.musicplayer.navigation.NavigationHandler
import uz.gita.musicplayer.presentation.ui.screens.language.LanguageScreen
import uz.gita.musicplayer.presentation.ui.screens.main.MainScreen
import uz.gita.musicplayer.presentation.ui.theme.MusicPlayerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: NavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme(darkTheme = ThemeUtil.value.value) {
                Navigator(screen = MainScreen()) { navigator ->
                    LaunchedEffect(key1 = navigator) {
                        navigationHandler.navigatorState.onEach {
                            it.invoke(navigator)
                        }.launchIn(lifecycleScope)
                    }
                    CurrentScreen()
                }
            }

        }

    }

}