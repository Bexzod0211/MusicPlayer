package uz.gita.musicplayer.presentation.ui.contract

import android.content.Context
import org.orbitmvi.orbit.ContainerHost
import uz.gita.musicplayer.data.model.MusicData

interface MainContract {
    sealed interface Intent {
        class LoadAllData(val context:Context):Intent
        class ItemClickedByPos(val pos:Int):Intent
    }

    sealed interface UiState {
        class PreparedData(val musics:List<MusicData> = listOf(),val tabs:List<Int> = listOf()):UiState
    }

    sealed interface SideEffect {
        object StartMusicService:SideEffect
        object OpenPermissionDialog:SideEffect
    }

    interface Direction {
        suspend fun openPlayScreen()
    }

    interface ViewModel : ContainerHost<UiState,SideEffect>{
        fun onEventDispatcher(intent:Intent)
    }
}