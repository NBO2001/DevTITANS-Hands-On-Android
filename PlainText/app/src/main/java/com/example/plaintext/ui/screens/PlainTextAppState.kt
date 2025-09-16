package com.example.plaintext.ui.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.plaintext.data.model.PasswordInfo
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen() {

  @Serializable object Login

  @Serializable data class Hello(val name: String?)

  @Serializable object Preferences

  @Serializable object List

  @Serializable data class EditList(val password: PasswordInfo)

  @Serializable object sensors
}

@Composable
fun rememberPlainTextAppState(
        navController: NavHostController = rememberNavController(),
        context: Context = LocalContext.current
) = remember(navController, context) { PlainTextAppState(navController, context) }

class PlainTextAppState(val navController: NavHostController, private val context: Context) {

  fun checkRoute(route: String): Boolean {
    val currentRoute = navController.currentBackStackEntry?.destination?.route.toString()

    return currentRoute != route
  }

  fun navigateToHello(name: String?) {
    navController.navigate(Screen.Hello(name))
  }

  fun navigateToLogin() {
    navController.navigate(Screen.Login)
  }

  fun navigateToListPasswords() {
    navController.navigate(Screen.List)
  }

  fun navigateToEditPassword(password: PasswordInfo) {
    navController.navigate(Screen.EditList(password))
  }

  fun navigateToSettings() {
    navController.navigate(Screen.Preferences)
  }

  fun back() {
    navController.popBackStack()
  }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
        this.lifecycle.currentState == Lifecycle.State.RESUMED

//this.lifecycle.currentState == Lifecycle.State.RESUMED
