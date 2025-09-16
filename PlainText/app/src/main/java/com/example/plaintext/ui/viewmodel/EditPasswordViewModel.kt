package com.example.plaintext.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {
    fun savePassword(password: PasswordInfo) {
        viewModelScope.launch {
            if (password.id > 0) {
               updatePassword(password)
            } else {
                passwordDBStore.add(password.toPassword())
            }
        }
    }

    fun updatePassword(password: PasswordInfo) {
        viewModelScope.launch {
            passwordDBStore.update(password.toPassword())
        }
    }
}