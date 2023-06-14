package uz.gita.musicplayer.presentation.ui.contract

import android.content.Context
import org.orbitmvi.orbit.ContainerHost
import uz.gita.musicplayer.data.model.CommandEnum
import uz.gita.musicplayer.data.model.MusicData

interface MainContract {
    sealed interface Intent {
        class LoadAllData(val context:Context):Intent
        class ItemClickedByPos(val pos:Int):Intent
        object BottomContentClicked:Intent
        class DoCommand(val command:CommandEnum):Intent
    }

    sealed interface UiState {
        class PreparedData(val musics:List<MusicData> = listOf(),val tabs:List<Int> = listOf(),val music:MusicData? = null):UiState
    }

    sealed interface SideEffect {
        class StartMusicService(val command:CommandEnum) :SideEffect
        object OpenPermissionDialog:SideEffect
    }

    interface Direction {
        suspend fun openPlayScreen()
    }

    interface ViewModel : ContainerHost<UiState,SideEffect>{
        fun onEventDispatcher(intent:Intent)
    }
}