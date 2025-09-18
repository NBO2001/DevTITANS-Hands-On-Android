// PreferencesRepository.kt
package com.example.plaintext.data

import com.example.plaintext.ui.viewmodel.PreferencesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor() {
    private val _preferencesState = MutableStateFlow(
        PreferencesState(login = "devtitans", password = "123", preencher = true)
    )
    val preferencesState: StateFlow<PreferencesState> = _preferencesState

    fun updateLogin(login: String) {
        _preferencesState.value = _preferencesState.value.copy(login = login)
    }
    fun updatePassword(password: String) {
        _preferencesState.value = _preferencesState.value.copy(password = password)
    }
    fun updatePreencher(preencher: Boolean) {
        _preferencesState.value = _preferencesState.value.copy(preencher = preencher)
    }

    fun checkCredentials(login: String, password: String): Boolean {
        val s = _preferencesState.value
        return login == s.login && password == s.password
    }
}
