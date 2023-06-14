package uz.gita.musicplayer.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.musicplayer.presentation.ui.contract.PlayContract
import uz.gita.musicplayer.presentation.ui.usecase.PlayUseCase
import uz.gita.musicplayer.utils.MyEventBus
import uz.gita.musicplayer.utils.base.getMusicByPos
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val useCase: PlayUseCase,
    private val direction:PlayContract.Direction
) : PlayContract.ViewModel, ViewModel() {


    override val container: Container<PlayContract.UiState, PlayContract.SideEffect> = container(PlayContract.UiState.CurrentMusicData(MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos)))

    init {
        intent {
            reduce {
//                myLog("reduce -> music = ${MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos)}")
                PlayContract.UiState.CurrentMusicData(music = MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos))
            }
        }
    }

    override fun onEventDispatcher(intent: PlayContract.Intent) {
        when (intent) {
            PlayContract.Intent.LoadMusicData->{
                intent {
                    reduce {
                        PlayContract.UiState.CurrentMusicData(MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos))
                    }
                }
            }
            is PlayContract.Intent.DoCommand->{
                intent {
                    postSideEffect(PlayContract.SideEffect.DoCommandOnService(intent.command))
                }
            }

            is PlayContract.Intent.BackToMain->{
                viewModelScope.launch {
                    direction.backToMain()
                }
            }
            is PlayContract.Intent.Shuffle->{
                MyEventBus.isShuffle = intent.shuffleState
            }
        }
    }


}