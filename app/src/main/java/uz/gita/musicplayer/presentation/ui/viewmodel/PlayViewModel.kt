package uz.gita.musicplayer.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.source.local.entity.MusicEntity
import uz.gita.musicplayer.presentation.ui.contract.PlayContract
import uz.gita.musicplayer.presentation.ui.usecase.PlayUseCase
import uz.gita.musicplayer.utils.MyEventBus
import uz.gita.musicplayer.utils.base.getMusicByPos
import uz.gita.musicplayer.utils.myLog
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val useCase: PlayUseCase,
    private val direction: PlayContract.Direction
) : PlayContract.ViewModel, ViewModel() {
    val musicData: MusicData
        get() = if (MyEventBus.selectedPos != -1) MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos)
         else useCase.getAllFavouriteMusics()[MyEventBus.selectedFavPos]

    val currentMusic: MusicEntity
        get() = MusicEntity(0, musicData.musicId, musicData.name, musicData.artist, musicData.albumId, musicData.data, musicData.duration, true)

    override val container: Container<PlayContract.UiState, PlayContract.SideEffect> =
        container(PlayContract.UiState.CurrentMusicData(musicData, false))

    init {
        /*intent {
            reduce {
//                myLog("reduce -> music = ${MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos)}")
                PlayContract.UiState.CurrentMusicData(music = MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos), isFavourite = isFavourite())
            }
        }*/
    }



    private  fun isFavourite(): Boolean {
        useCase.getAllFavouriteMusics().forEach {
            if (it.musicId == musicData.musicId)
                return true
        }
        return false
    }

    override fun onEventDispatcher(intent: PlayContract.Intent) {
        when (intent) {
            PlayContract.Intent.LoadMusicData -> {
                intent {
                    reduce {
                        myLog("PlayContract.Intent.LoadMusicData, isFavourite=> ${isFavourite()}")
                        PlayContract.UiState.CurrentMusicData(musicData, isFavourite())
                    }
                }
            }

            is PlayContract.Intent.DoCommand -> {
                intent {
                    postSideEffect(PlayContract.SideEffect.DoCommandOnService(intent.command))
                }
            }

            is PlayContract.Intent.BackToMain -> {
                viewModelScope.launch {
                    direction.backToMain()
                }
            }

            is PlayContract.Intent.Shuffle -> {
                MyEventBus.isShuffle = intent.shuffleState
            }

            PlayContract.Intent.AddToFavourites -> {
                useCase.addMusic(currentMusic)
                    .onEach {

                    }.launchIn(viewModelScope)
            }

            PlayContract.Intent.RemoveFromFavourites -> {
                useCase.deleteMusicByMusicId(musicData.musicId)
                    .onEach {

                    }
                    .launchIn(viewModelScope)
            }
        }
    }


}