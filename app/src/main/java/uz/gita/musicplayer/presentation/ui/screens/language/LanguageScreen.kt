package uz.gita.musicplayer.presentation.ui.screens.language

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.musicplayer.R
import uz.gita.musicplayer.data.model.Language
import uz.gita.musicplayer.data.model.ThemeEnum
import uz.gita.musicplayer.data.model.ThemeUtil
import uz.gita.musicplayer.presentation.ui.screens.main.MainScreen
import uz.gita.musicplayer.presentation.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer.presentation.ui.viewmodel.LanguageViewModel
import uz.gita.musicplayer.utils.myLog
import java.util.Locale


class LanguageScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<LanguageViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val configuration = Configuration()
        val context = LocalContext.current
        updateLocal(configuration, context, viewModel.repository.getLanguage())

        var value by remember {
            mutableStateOf(0)
        }

        val scope = CoroutineScope(Dispatchers.Main)

        value
        LanguageScreenContent(onLangClick = {
            when(it){
                Language.ENG->{
                    viewModel.repository.setLanguage("")
                    updateLocal(configuration,context,"")
                    value++
                }
                Language.RU->{
                    viewModel.repository.setLanguage("ru")
                    updateLocal(configuration,context,"ru")
                    value++
                }
            }

        },
            onThemeClick = {
                when(it){
                    ThemeEnum.DARK->{
                        ThemeUtil.value.value = true
                    }
                    ThemeEnum.LIGHT->{
                        ThemeUtil.value.value = false
                    }
                }
                
                scope.launch {
                    delay(300)
                    navigator.push(MainScreen())
                }
            })
        value++
    }

    private fun updateLocal(configuration: Configuration,context:Context,lang:String){
        val locale = Locale(lang)
        configuration.setLocale(locale)
        val resources =context.resources
        resources.updateConfiguration(configuration,resources.displayMetrics)
    }
}

@Composable
private fun LanguageScreenContent(onLangClick: (Language) -> Unit,onThemeClick:(ThemeEnum)->Unit) {
    MusicPlayerTheme(darkTheme = ThemeUtil.value.value) {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(id = R.string.text_welcome), fontSize = 32.sp, modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Row {
                        Column {
                            Button(onClick = {
                                onLangClick.invoke(Language.ENG)
                            }) {
                                Text(text = "English", fontSize = 24.sp)
                            }
                            Button(
                                onClick = {
                                    onLangClick.invoke(Language.RU)
                                },
                                modifier = Modifier.padding(top = 16.dp),

                                ) {
                                Text(text = "Русский", fontSize = 24.sp)
                            }
                        }
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Button(onClick = {
                                onThemeClick.invoke(ThemeEnum.DARK)
                            }) {
                                myLog("Text Dark")
                                Text(text = stringResource(id = R.string.txt_dark), fontSize = 24.sp)
                            }
                            Button(
                                onClick = {
                                    onThemeClick.invoke(ThemeEnum.LIGHT)
                                },
                                modifier = Modifier.padding(top = 16.dp)) {
                                myLog("Text Light")
                                Text(text = stringResource(id = R.string.txt_light), fontSize = 24.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageScreenPreview() {
    LanguageScreenContent({},{})
}