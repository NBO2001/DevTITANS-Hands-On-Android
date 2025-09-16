package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var loginText by mutableStateOf("")
        private set

    var passwordText by mutableStateOf("")
        private set

    var rememberMe by mutableStateOf(false)
        private set

    fun onLoginTextChange(newText: String) {
        loginText = newText
    }

    fun onPasswordTextChange(newText: String) {
        passwordText = newText
    }

    fun onRememberMeChange(checked: Boolean) {
        rememberMe = checked
    }

    // Esta função será para a lógica de autenticação real.
    // Por enquanto, apenas um log simples.
    fun onLoginClick(onSuccess: () -> Unit, onError: (message: String) -> Unit) {
        // Logica de autenticação ficará aqui

        if (loginText == "admin" && passwordText == "password") {
            // Sucesso
            onSuccess()
        } else {
            // Falha
            onError("Login ou senha inválidos.")
        }
    }
}