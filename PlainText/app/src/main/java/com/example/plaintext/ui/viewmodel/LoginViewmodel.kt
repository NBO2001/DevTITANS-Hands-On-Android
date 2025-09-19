package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val repo: PreferencesRepository
) : ViewModel() {

  var loginText by mutableStateOf("")
    private set

  var passwordText by mutableStateOf("")
    private set

  var rememberMe by mutableStateOf(false)
    private set

  init {
    viewModelScope.launch {
      repo.preferencesState.collect { prefs ->
        loginText = if (prefs.preencher) prefs.login else ""
        passwordText = if (prefs.preencher) prefs.password else ""
        rememberMe = prefs.preencher
      }
    }
  }

  fun onLoginTextChange(newText: String) {
    loginText = newText
  }

  fun onPasswordTextChange(newText: String) {
    passwordText = newText
  }

  fun onRememberMeChange(checked: Boolean) {
    rememberMe = checked
    repo.updatePreencher(checked)
  }

  // Esta função será para a lógica de autenticação real.
  // Por enquanto, apenas um log simples.
  fun onLoginClick(onSuccess: () -> Unit, onError: (message: String) -> Unit) {
    // Logica de autenticação ficará aqui

    if (repo.checkCredentials(loginText, passwordText)) {
      // Sucesso
      onSuccess()
    } else {
      // Falha
      onError("Login ou senha inválidos.")
    }
  }
}

