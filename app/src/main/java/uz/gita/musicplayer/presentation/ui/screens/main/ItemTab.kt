package uz.gita.musicplayer.presentation.ui.screens.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.gita.musicplayer.R

@Composable
fun ItemTab(@StringRes value:Int){
    Box {
       Text(text = stringResource(id = value),fontSize = 24.sp,modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ItemTabPreview() {
    ItemTab(R.string.text_welcome)
}