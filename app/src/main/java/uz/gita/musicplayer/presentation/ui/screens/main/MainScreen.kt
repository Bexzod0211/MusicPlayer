package uz.gita.musicplayer.presentation.ui.screens.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import coil.compose.rememberAsyncImagePainter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.musicplayer.MainActivity
import uz.gita.musicplayer.R
import uz.gita.musicplayer.data.model.CommandEnum
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.presentation.ui.contract.MainContract
import uz.gita.musicplayer.presentation.ui.theme.Gray
import uz.gita.musicplayer.presentation.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer.presentation.ui.theme.TopBar
import uz.gita.musicplayer.presentation.ui.viewmodel.MainViewModel
import uz.gita.musicplayer.service.MusicService
import uz.gita.musicplayer.utils.MyEventBus
import uz.gita.musicplayer.utils.base.checkPermissions
import uz.gita.musicplayer.utils.component.MarqueeTextComponent
import uz.gita.musicplayer.utils.myLog

class MainScreen : AndroidScreen() {
    val READ_AUDIO = 1

    @Composable
    override fun Content() {

        val context = LocalContext.current as MainActivity
        val viewModel: MainContract.ViewModel = getViewModel<MainViewModel>()

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                MainContract.SideEffect.OpenPermissionDialog -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.checkPermissions(arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.POST_NOTIFICATIONS)) {
                            viewModel.onEventDispatcher(MainContract.Intent.LoadAllData(context))
                        }
                    } else {
                        context.checkPermissions(arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            viewModel.onEventDispatcher(MainContract.Intent.LoadAllData(context))
                        }
                    }
                }

                is MainContract.SideEffect.StartMusicService -> {
//                    myLog("MainContract.SideEffect.StartMusicService")
                    val intent = Intent(context, MusicService::class.java)
                    intent.putExtra("COMMAND", sideEffect.command)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        myLog("starting foreground service from mainscreen")
                        context.startForegroundService(intent)
                    } else {
//                        myLog("starting background service from mainscreen")
                        context.startService(intent)
                    }
                }

            }
        }

        MusicPlayerTheme {
            MainScreenContent(uiState = viewModel.collectAsState(), onEventDispatcher = viewModel::onEventDispatcher)
        }

        /*if (ContextCompat.checkSelfPermission(LocalContext.current,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            MainScreenContent()
        }
        else {
            (LocalContext.current as Activity).requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),READ_AUDIO)
        }*/
    }


}

@Composable
private fun MainScreenContent(
    uiState: State<MainContract.UiState>,
    onEventDispatcher: (MainContract.Intent) -> Unit
) {

    val context = LocalContext.current

    onEventDispatcher.invoke(MainContract.Intent.LoadAllData(context))
//    myLog("MainScreenContent $${(uiState.value as MainContract.UiState.PreparedData).music}")
    val isPlaying = MyEventBus.musicIsPlaying.collectAsState()
    var change by remember {
        mutableStateOf(true)
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        when (uiState.value) {
            is MainContract.UiState.PreparedData -> {
                val data = uiState.value as MainContract.UiState.PreparedData
                Box(modifier = Modifier.fillMaxSize()) {
                    /*LazyRow(modifier = Modifier.padding(vertical = 4.dp).align(Alignment.TopCenter)) {
                        data.tabs.forEach { tab ->
                            item {
//                                myLog("tab : ${stringResource(id = tab)}")
                                ItemTab(value = tab)
                            }
                        }
                    }*/
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(TopBar)

                    ) {
                        Row(modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Music Player",
                                fontSize = 24.sp,
                                color = Gray,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                            )
                        }

                        TabLayout(allMusics = data.musics, favourites = data.favouriteMusics, onEventDispatcher = onEventDispatcher,
                        onChange = {
                            change = !change
                        })
                    }

                    BottomComponent(modifier = Modifier.align(Alignment.BottomCenter), isPlaying.value, onEventDispatcher,change)
                }
            }
        }
    }
}

private val tabs = listOf(
    "All Tracks",
    "Favourites"
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabLayout(
    allMusics: List<MusicData>,
    favourites: List<MusicData>,
    onEventDispatcher: (MainContract.Intent) -> Unit,
    onChange:()->Unit
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    var change by remember {
        mutableStateOf(true)
    }

    change

    var selectedMusic = if (MyEventBus.selectedPos !=-1) MyEventBus.selectedMusicFlow.collectAsState()
    else  MyEventBus.selectedFavMusicFlow.collectAsState()

            TabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                tabs.forEachIndexed { index, item ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(
                                text = item,
                                fontSize = 20.sp
                            )
                        },
                        modifier = Modifier
                            .background(TopBar)
                        )
                }
            }

        when (selectedTabIndex) {
            0 -> {
                // Content for Tab 1
                LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = 70.dp
                    )
                ) {
                    for (pos in allMusics.indices) {
                        item {
                            val item = allMusics[pos]
                            var isSelected = false
                            if (MyEventBus.selectedPos != -1) {
                                isSelected = item == selectedMusic.value
                            }
                            ItemMusic(music = item, onClickItem = {
//                                myLog("item clicked pos : $pos")
                                onEventDispatcher.invoke(MainContract.Intent.AllTracksItemClickedByPos(pos))
                                change = !change
                                onChange.invoke()
                            }, isSelected)
                        }
                    }
                }
            }

            1 -> {
                // Content for Tab 2
               if (favourites.isEmpty()){
                   Column(modifier = Modifier
                       .fillMaxSize(),
                   verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally) {
                       Text(
                           text = "No Favourite Musics",
                           fontSize = 24.sp,
                           color = Gray
                       )
                   }
               }
                else {
                   LazyColumn(
                       contentPadding = PaddingValues(
                           bottom = 70.dp
                       ),
                       modifier = Modifier
                           .fillMaxSize()
                           .background(Color.White)
                   ) {
                       for (pos in favourites.indices) {
                           item {
                               val item = favourites[pos]
                               var isSelected = false
                               if (MyEventBus.selectedFavPos != -1) {
                                   isSelected = item == selectedMusic.value
                               }
                               ItemMusic(music = item, onClickItem = {
//                                   myLog("onClickItem Fav in MainScreen")
                                   onEventDispatcher.invoke(MainContract.Intent.FavouritesItemClickByPos(pos))
                                   change =!change
                                   onChange.invoke()
                               }, isSelected)
                           }
                       }
                   }
               }
            }
        }
    }

@Composable
private fun BottomComponent(modifier: Modifier, isPlaying: Boolean, onUserAction: (MainContract.Intent) -> Unit,change:Boolean) {
    /*var change = MyEventBus.changeFlow.collectAsState()
    change*/
    change
    var selectedFavMusic = MyEventBus.selectedFavMusicFlow.collectAsState()
    var selectedMusic = MyEventBus.selectedMusicFlow.collectAsState()
//    var selectedMusicState = if (MyEventBus.selectedPos !=-1) MyEventBus.selectedMusicFlow.collectAsState()
//    else  MyEventBus.selectedFavMusicFlow.collectAsState()
    val music = if (MyEventBus.selectedPos != - 1) selectedMusic.value
    else selectedFavMusic.value

//    myLog("BottomComponent")

    val mName = music?.name ?: "No composition"
    val mArtist = music?.artist ?: ""
    val albumId = music?.albumId
    val uri = Uri.parse("content://media/external/audio/albumart/$albumId")
    val image = if (music?.albumId == 6539316500227728566 || music?.albumId == 2138260763037359727 || music == null) {
        painterResource(id = R.drawable.ic_music)
    } else {
        rememberAsyncImagePainter(uri)
    }

    Card(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .clickable {
                onUserAction.invoke(MainContract.Intent.BottomContentClicked)
            },
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier
                .heightIn(50.dp)
                .fillMaxWidth()
                .background(Color(0xFF3AC7FE)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(50.dp)
                    .clip(RoundedCornerShape(25.dp))
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 4.dp)
                    .height(70.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                MarqueeTextComponent(
                    myText = mName,
                    color = Color.White,
                    size = 20.sp,
                    fontWeight = FontWeight.Bold,
                    start = 0.dp,
                    end = 0.dp,
                    top = 0.dp,
                )
                MarqueeTextComponent(
                    myText = mArtist,
                    color = Color.White,
                    size = 17.sp,
                    start = 0.dp,
                    end = 0.dp,
                    top = 0.dp
                )
            }

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onUserAction.invoke(MainContract.Intent.DoCommand(CommandEnum.PREV))
                    },
                painter = painterResource(id = R.drawable.ic_prev),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        onUserAction.invoke(MainContract.Intent.DoCommand(CommandEnum.MANAGE))
                    },
                painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 16.dp
                    )
                    .clickable {
                        onUserAction.invoke(MainContract.Intent.DoCommand(CommandEnum.NEXT))
                    },
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null
            )

         /*   Image(
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp),
                painter = painterResource(id = R.drawable.ic_musics),
                contentDescription = null
            )*/
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreenContent(remember {
        mutableStateOf(MainContract.UiState.PreparedData())
    }, {})
}

