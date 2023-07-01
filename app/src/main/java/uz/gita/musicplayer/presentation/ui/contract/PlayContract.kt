package uz.gita.musicplayer.presentation.ui.contract

import org.orbitmvi.orbit.ContainerHost
import uz.gita.musicplayer.data.model.CommandEnum
import uz.gita.musicplayer.data.model.MusicData

interface PlayContract {
    sealed interface Intent {
        object LoadMusicData:Intent
        class DoCommand(val command:CommandEnum):Intent
        object BackToMain:Intent
        class Shuffle(val shuffleState:Boolean):Intent
        object AddToFavourites:Intent
        object RemoveFromFavourites:Intent
    }

    sealed interface UiState {
        class CurrentMusicData(val music:MusicData,val isFavourite:Boolean):UiState
    }

    sealed interface SideEffect {
        class DoCommandOnService(val command:CommandEnum):SideEffect
    }

    interface Direction {
        suspend fun backToMain()
    }

    interface ViewModel : ContainerHost<UiState,SideEffect>{
        fun onEventDispatcher(intent:Intent)
    }

}