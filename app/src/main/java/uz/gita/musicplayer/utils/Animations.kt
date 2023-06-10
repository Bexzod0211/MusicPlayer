package uz.gita.musicplayer.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.gita.musicplayer.R
import uz.gita.musicplayer.presentation.ui.theme.MusicPlayerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                ValueBasedAnim()
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityContent() {

    Column(modifier = Modifier.fillMaxSize()) {
        var isVisible by remember { mutableStateOf(false) }

        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "Animate")
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(animationSpec = spring(Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessVeryLow), initialScale = 0.5f) + fadeIn(animationSpec = tween(1000)),
            exit = scaleOut(animationSpec = tween(2000), targetScale = 0f) + fadeOut(animationSpec = tween(1000))
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(color = Color.LightGray)
            )
        }
    }
}

@Composable
fun ValueBasedAnim() {
    var state by remember { mutableStateOf(false) }
    var counter by remember { mutableStateOf(1) }
    val rotate by animateFloatAsState(
        targetValue = if (state) 360f else 0f,
        animationSpec = keyframes {
            durationMillis = 2000
            90f at 0 with LinearOutSlowInEasing
            180f at 500 with FastOutLinearInEasing
            270f at 1500 with LinearEasing
            360f at 2000 with LinearOutSlowInEasing
        }
    )

    ButtonContent(
        onClick = { state = !state },
        content = {
            Box(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .size(72.dp)
                    .rotate(rotate)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun ButtonContent(onClick: () -> Unit, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {

        Button(onClick = onClick) {
            Text(text = "Animate")
        }

        content()
    }
}