package com.example.plaintext.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.editList.Edit_screen
import com.example.plaintext.ui.screens.hello.Hello_screen
import com.example.plaintext.ui.screens.list.List_screen
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.ui.screens.preferences.SettingsScreen
import com.example.plaintext.utils.parcelableType
import kotlin.reflect.typeOf

@Composable
fun PlainTextApp(appState: PlainTextAppState = rememberPlainTextAppState()) {
  NavHost(
          navController = appState.navController,
          startDestination = Screen.Login,
  ) {
    composable<Screen.Hello> {
      var args = it.toRoute<Screen.Hello>()
      Hello_screen(args)
    }
    composable<Screen.Login> {
      Login_screen(
              navigateToSettings = { appState.navigateToSettings() },
              navigateToList = { appState.navigateToListPasswords() }
      )
    }
    composable<Screen.List> { List_screen(appState) }
    composable<Screen.EditList>(
            typeMap = mapOf(typeOf<PasswordInfo>() to parcelableType<PasswordInfo>())
    ) {
      val args = it.toRoute<Screen.EditList>()
      Edit_screen(args, appState)
    }

    composable<Screen.Preferences> {
      SettingsScreen(navController = appState.navController)
    }
  }
}
