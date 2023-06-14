package uz.gita.musicplayer.utils.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarqueeTextComponent(
    myText: String,
    color: Color = Color.Black,
    size: TextUnit = 22.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    start: Dp = 16.dp,
    end:Dp = 16.dp,
    top:Dp = 24.dp
) {
    val scrollState = rememberScrollState()
    var shouldAnimated by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = shouldAnimated) {
        scrollState.animateScrollTo(
            scrollState.maxValue,
            animationSpec = tween(15000, 700, easing = CubicBezierEasing(0f, 0f, 0f, 0f))
        )
        scrollState.scrollTo(0)
        shouldAnimated = !shouldAnimated
    }
    Text(
        text = myText,
        color = color,
        fontSize = size,
        overflow = TextOverflow.Ellipsis,
        fontWeight = fontWeight,
        maxLines = 1,
        modifier = Modifier
            .horizontalScroll(scrollState, false)
            .padding(start = start, end = end, top = top)
        ,
    )
}