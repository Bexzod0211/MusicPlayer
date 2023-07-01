package uz.gita.musicplayer.presentation.ui.screens.play

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import coil.compose.rememberAsyncImagePainter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.musicplayer.R
import uz.gita.musicplayer.data.model.CommandEnum
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.model.ThemeUtil
import uz.gita.musicplayer.presentation.ui.contract.PlayContract
import uz.gita.musicplayer.presentation.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer.presentation.ui.viewmodel.PlayViewModel
import uz.gita.musicplayer.service.MusicService
import uz.gita.musicplayer.utils.MyEventBus
import uz.gita.musicplayer.utils.component.MarqueeTextComponent

class PlayScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel: PlayContract.ViewModel = getViewModel<PlayViewModel>()
        val context = LocalContext.current
        viewModel.collectSideEffect {
            when (it) {
                is PlayContract.SideEffect.DoCommandOnService -> {
                    startService(context, it.command)
                }
            }
        }

        PlayScreenContent(viewModel.collectAsState(), viewModel::onEventDispatcher)
    }

    private fun startService(context: Context, command: CommandEnum) {
        val intent = Intent(context, MusicService::class.java)
        intent.putExtra("COMMAND", command)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}

private fun durationToTime(duration: Long): String {
    val totalSeconds = (duration / 1000).toInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds - hours * 3600) / 60
    val seconds = totalSeconds % 60

    val s = if (seconds < 10) {
        "0$seconds"
    } else "$seconds"

    val result = if (hours != 0) {
        val m = if (minutes < 10) {
            "0$minutes"
        } else "$minutes"
        "$hours:$m:$s"
    } else {
        "$minutes:$s"
    }


    return result
}

@Composable
private fun PlayScreenContent(uiState: State<PlayContract.UiState>, onEventDispatcher: (PlayContract.Intent) -> Unit) {


//    context.window.enterTransition = Fade(Fade.MODE_IN)
    onEventDispatcher.invoke(PlayContract.Intent.LoadMusicData)

    var currentTime = MyEventBus.currentTimeFlow.collectAsState()
//    myLog("PlayScreenContent")
//    myLog("${uiState.value}")
    val isPlaying = MyEventBus.musicIsPlaying.collectAsState()
    var shuffle by remember {
        mutableStateOf(MyEventBus.isShuffle)
    }
    var isRepeatOne by remember {
        mutableStateOf(MyEventBus.isRepeatOne)
    }

    
    val imageSize = animateDpAsState(targetValue = if (isPlaying.value) 320.dp else 170.dp, animationSpec = tween(durationMillis = 300))

    when (uiState.value) {
        is PlayContract.UiState.CurrentMusicData -> {
//            myLog("PlayScreen: ${(uiState.value as PlayContract.UiState.CurrentMusicData).music}")
            val currentMusicData = uiState.value as PlayContract.UiState.CurrentMusicData
            val music = currentMusicData.music

            var isFavourite by remember {
                mutableStateOf(currentMusicData.isFavourite)
            }
            isFavourite = currentMusicData.isFavourite

            val albumId = music.albumId
            val uri = Uri.parse("content://media/external/audio/albumart/$albumId")
            val image = if (music.albumId == 6539316500227728566 || music.albumId == 2138260763037359727) {
                painterResource(id = R.drawable.ic_music)
            } else {
                rememberAsyncImagePainter(uri)
            }

            val totalTime = durationToTime(music.duration)
            MusicPlayerTheme(darkTheme = ThemeUtil.value.value) {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    Column(
                        modifier = Modifier
                            .background(brush = Brush.horizontalGradient(arrayListOf(Color(0xFF60D1FF), Color(0xFFEDA7CA))))
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_more_down),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .clickable {
                                        onEventDispatcher.invoke(PlayContract.Intent.BackToMain)
                                    }
                                    .padding(4.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Image(
                                painter = if (isFavourite) painterResource(id = R.drawable.ic_favourite)
                                else painterResource(id = R.drawable.ic_favourite_border),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable {
                                        if (isFavourite) onEventDispatcher.invoke(PlayContract.Intent.RemoveFromFavourites)
                                        else onEventDispatcher.invoke(PlayContract.Intent.AddToFavourites)
                                        isFavourite = !isFavourite
                                    }
                            )
                            /*Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                                Image(painter = painterResource(id = R.drawable.ic_volume),
                                    contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(4.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_more),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color(0xFF252525)),
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .clickable {

                                        }
                                        .padding(4.dp)
                                )
                            }*/
                        }
                        Box(
                            modifier = Modifier
                                .padding(top = 48.dp)
                                .align(Alignment.CenterHorizontally)
                                .size(imageSize.value)
                                .clip(RoundedCornerShape(32.dp))
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = image,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .fillMaxWidth()){
                            MarqueeTextComponent(myText = music.name)
                        }
                        Text(
                            text = music.artist,
                            color = Color(0xFF807F85),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .align(Alignment.CenterHorizontally),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .padding(start = 24.dp, end = 24.dp)
                                    .fillMaxWidth()
                            ) {
                                /*Image(
                                    painter = painterResource(id = R.drawable.ic_musics),
                                    contentDescription = null
                                )*/
                                /*Image(
                                    painter = if (isFavourite) painterResource(id = R.drawable.ic_favourite)
                                    else painterResource(id = R.drawable.ic_favourite_border),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clickable {
                                            if (isFavourite) onEventDispatcher.invoke(PlayContract.Intent.RemoveFromFavourites)
                                            else onEventDispatcher.invoke(PlayContract.Intent.AddToFavourites)
                                            isFavourite = !isFavourite
                                        }
                                )*/
                                /*Image(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = null
                                )*/
                            }
                            Slider(
                                value = currentTime.value.toFloat(),
                                onValueChange = {
                                    MyEventBus.currentTime = it.toInt()
                                    onEventDispatcher.invoke(PlayContract.Intent.DoCommand(CommandEnum.SEEKBAR_CHANGED))
                                },
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                                colors = SliderDefaults.colors(
                                    activeTrackColor = Color(0xFFFAFAFA),
                                    inactiveTickColor = Color(0x1BFFFFFF),
                                    inactiveTrackColor = Color(0x25FFFFFF),
                                    thumbColor = Color(0xFFFAFAFA)
                                ),
                                steps = 1000,
                                valueRange = 0f..music.duration.toFloat(),
                                onValueChangeFinished = {
                                    onEventDispatcher.invoke(PlayContract.Intent.DoCommand(CommandEnum.SEEK_TO))
                                }
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .padding(start = 24.dp, end = 24.dp, top = 0.dp)
                                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = durationToTime(currentTime.value.toLong()), color = Color(0xA8FFFFFF))
                                Text(text = totalTime, color = Color(0xA8FFFFFF))
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 60.dp)
                                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = if (shuffle) R.drawable.shuffle_on
                                    else R.drawable.shuffle_off),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            shuffle = !shuffle
                                            onEventDispatcher.invoke(PlayContract.Intent.Shuffle(shuffle))
                                        },
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_prev),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable {
                                            onEventDispatcher.invoke(PlayContract.Intent.DoCommand(CommandEnum.PREV))
                                        }
                                )
                                Image(
                                    painter = painterResource(
                                        id = if (isPlaying.value) {
                                            R.drawable.ic_pause
                                        } else {
                                            R.drawable.ic_play
                                        }
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable {
                                            onEventDispatcher.invoke(PlayContract.Intent.DoCommand(CommandEnum.MANAGE))
                                        }
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_next),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable {
                                            onEventDispatcher.invoke(PlayContract.Intent.DoCommand(CommandEnum.NEXT))
                                        }
                                )
                                Image(
                                    painter = painterResource(id =
                                    if(isRepeatOne) R.drawable.ic_repeat
                                    else R.drawable.ic_repeat_all),
                                    contentDescription = null,
                                    modifier = Modifier.
                                    clickable {
                                        isRepeatOne = !isRepeatOne
                                        MyEventBus.isRepeatOne = isRepeatOne
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PlayScreenPreview() {
    PlayScreenContent(remember {
        mutableStateOf(PlayContract.UiState.CurrentMusicData(MusicData(1, "Another love", "Tom Odell", 1L, "", 12000),true))
    }, {})
}