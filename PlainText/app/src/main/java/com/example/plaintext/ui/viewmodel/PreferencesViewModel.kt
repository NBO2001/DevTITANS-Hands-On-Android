package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.plaintext.data.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreferencesState(
    var login: String,
    var password: String,
    var preencher: Boolean
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val repo: PreferencesRepository
) : ViewModel() {
    var preferencesState by mutableStateOf(repo.preferencesState.value)
        private set

    init {
        viewModelScope.launch {
            repo.preferencesState.collect { preferencesState = it }
        }
    }
    fun updateLogin(login: String) = repo.updateLogin(login)
    fun updatePassword(password: String) = repo.updatePassword(password)
    fun updatePreencher(preencher: Boolean) = repo.updatePreencher(preencher)

    fun getPreencher() = repo.preferencesState.value.preencher

}