package uz.gita.musicplayer.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.musicplayer.domain.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    val repository: AppRepository
) : ViewModel(){

}