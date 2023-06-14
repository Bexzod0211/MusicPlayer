package uz.gita.musicplayer.presentation.ui.screens.main

import android.net.Uri
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import uz.gita.musicplayer.R
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.model.ThemeUtil
import uz.gita.musicplayer.presentation.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer.presentation.ui.theme.Selected
import uz.gita.musicplayer.utils.myLog

@Composable
fun ItemMusic(music:MusicData,onClickItem:()->Unit,isSelected:Boolean = false) {
//    myLog("name:${music.name}  image:${music.albumId}")

    val albumId = music.albumId
    val uri = Uri.parse("content://media/external/audio/albumart/$albumId")
    val image = if (music.albumId == 6539316500227728566 || music.albumId == 2138260763037359727){
        painterResource(id = R.drawable.ic_music)
    }else {
        rememberAsyncImagePainter(uri)
    }
    var mNameColor = Color.Black
    var mArtistColor = Color.Gray

    if (isSelected){
        mNameColor = Selected
        mArtistColor = Selected
    }

    MusicPlayerTheme(darkTheme = ThemeUtil.value.value) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier
                .heightIn(80.dp)
                .padding(vertical = 2.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = rememberRipple(
                    radius = 232.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                ), onClick = {
                    myLog("onClick Column")
                    onClickItem.invoke()
                })) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(48.dp)
                            .align(Alignment.CenterVertically)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            painter = image,
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                    }
                    Column(
                        modifier = Modifier
                            .heightIn(80.dp)
                            .padding(start = 12.dp)
                            .weight(1f), verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = music.name,color = mNameColor, fontSize = 22.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(text = music.artist, color = mArtistColor, fontSize = 16.sp, maxLines = 1,overflow = TextOverflow.Ellipsis)
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_more), modifier = Modifier
                            .padding(end = 16.dp)
                            .size(30.dp)
                            .align(Alignment.CenterVertically), contentDescription = null
                    )
                }
                Divider(modifier = Modifier
                    .padding(start = 93.dp, top = 8.dp)
                    .heightIn(1.dp)
                    .fillMaxWidth(), thickness = 1.dp, color = Color(0xFFEBEBEB)
                )
            }
        }
    }
}
@Preview
@Composable
fun ItemMusicPreview() {
    ItemMusic(MusicData(1,"Another Love","Tom Odell",1,"",0),{})
}