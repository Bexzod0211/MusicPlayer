package uz.gita.musicplayer.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.musicplayer.data.model.CommandEnum
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.presentation.ui.contract.MainContract
import uz.gita.musicplayer.presentation.ui.usecase.MainUseCase
import uz.gita.musicplayer.utils.MyEventBus
import uz.gita.musicplayer.utils.base.getAllMusics
import uz.gita.musicplayer.utils.base.getMusicByPos
import uz.gita.musicplayer.utils.base.getMusicCursor
import uz.gita.musicplayer.utils.myLog
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MainUseCase,
    private val direction: MainContract.Direction
) : MainContract.ViewModel, ViewModel() {

    override val container = container<MainContract.UiState, MainContract.SideEffect>(MainContract.UiState.PreparedData())

    init {
        intent {
            postSideEffect(MainContract.SideEffect.OpenPermissionDialog)
        }
    }

    override fun onEventDispatcher(intent: MainContract.Intent) {
        when (intent) {
            is MainContract.Intent.LoadAllData -> {
                intent.context.getMusicCursor().onEach {
                    MyEventBus.cursor = it
                }.launchIn(viewModelScope)

                var favouriteMusics: List<MusicData> = listOf()
                useCase.getAllFavouriteMusicsFlow().onEach {
                    favouriteMusics = it
                }.launchIn(viewModelScope)

                intent {
                    reduce {
                        var tabs: List<Int> = listOf()

                        useCase.getAllTabs().onEach {
                            tabs = it
                        }.launchIn(viewModelScope)

                        MainContract.UiState.PreparedData(
                            intent.context.getAllMusics(),
                            tabs,
                            music = if (MyEventBus.selectedPos != -1) MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos)
                            else null,
                            favouriteMusics = favouriteMusics
                        )
                    }
                }
            }

            is MainContract.Intent.AllTracksItemClickedByPos -> {
                MyEventBus.selectedFavPos = -1
                MyEventBus.selectedPos = intent.pos
                MyEventBus.totalTime = MyEventBus.cursor!!.getMusicByPos(intent.pos).duration
                /*viewModelScope.launch {
                    direction.openPlayScreen()
                }*/
                intent {
                    postSideEffect(MainContract.SideEffect.StartMusicService(CommandEnum.START))
                }
            }

            is MainContract.Intent.BottomContentClicked -> {
                if (MyEventBus.selectedPos != -1 || MyEventBus.selectedFavPos != -1) {
                    viewModelScope.launch {
                        direction.openPlayScreen()
                    }
                }
            }

            is MainContract.Intent.DoCommand -> {
                intent {
                    if (MyEventBus.selectedPos != -1 || MyEventBus.selectedFavPos != -1)
                        postSideEffect(MainContract.SideEffect.StartMusicService(intent.command))
                }
            }

            is MainContract.Intent.FavouritesItemClickByPos -> {
//                myLog("MainContract.Intent.FavouritesItemClickByPos in viewModel")
                MyEventBus.selectedPos = -1
                MyEventBus.selectedFavPos = intent.pos
                MyEventBus.totalTime = useCase.getAllFavouriteMusics()[intent.pos].duration
                /*viewModelScope.launch {
                    direction.openPlayScreen()
                }*/
                intent {
//                    myLog("intent in viewModel")
                    postSideEffect(MainContract.SideEffect.StartMusicService(CommandEnum.START))
                }
            }
        }
    }

}