package com.example.plaintext.ui.screens.list

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plaintext.R
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.PlainTextAppState
import com.example.plaintext.ui.viewmodel.ListPasswordsViewModel
import com.example.plaintext.ui.viewmodel.ListViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun List_screen(
    appState: PlainTextAppState,
    viewModel: ListPasswordsViewModel = hiltViewModel()
) {
    val listState = viewModel.listViewState
    val context = LocalContext.current
    val message = stringResource(R.string.disabled_back_button)

    BackHandler(enabled = true) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopBarComponent(
                navigateToSettings = {
                    appState.navigateToSettings()
                },
                logoutAction = {
                    appState.navigateToLogin()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newPassword = PasswordInfo(
                        id = 0,
                        name = "",
                        login = "",
                        password = "",
                        notes = ""
                    )
                    Log.d("ListScreen", "Navigating to EditScreen with password: $newPassword")
                    appState.navigateToEditPassword(newPassword)
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_new_password)
                )
            }
        }
    ) { innerPadding ->
        ListContent(
            modifier = Modifier.padding(innerPadding),
            listState = listState,
            onItemClick = { password ->
                Log.d("ListScreen", "Navigating to EditScreen with password: $password")
                appState.navigateToEditPassword(password)
            }
        )
    }
}

@Composable
fun ListContent(
    modifier: Modifier = Modifier,
    listState: ListViewState,
    onItemClick: (PasswordInfo) -> Unit
) {
    when {
        !listState.isCollected -> LoadingScreen()
        listState.passwordList.isEmpty() -> EmptyListScreen()
        else -> PasswordList(
            modifier = modifier,
            passwords = listState.passwordList,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun PasswordList(
    modifier: Modifier = Modifier,
    passwords: List<PasswordInfo>,
    onItemClick: (PasswordInfo) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(passwords.size) { index ->
            PasswordItem(
                password = passwords[index],
                onClick = { onItemClick(passwords[index]) }
            )
        }
    }
}

@Composable
fun PasswordItem(
    password: PasswordInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.password_icon),
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = password.name,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )
            Text(
                text = password.login,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.navigate_to_edit),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun EmptyListScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.empty_list),
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.no_passwords_found),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.add_password_prompt),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarComponent(
    navigateToSettings: (() -> Unit?)? = null,
    logoutAction: (() -> Unit?)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val shouldShowDialog = remember { mutableStateOf(false) }

    if (shouldShowDialog.value) {
        MyAlertDialog(shouldShowDialog = shouldShowDialog)
    }

    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.menu_content)
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.settings)) },
                    onClick = {
                        navigateToSettings?.invoke()
                        expanded = false
                    },
                    modifier = Modifier.padding(8.dp)
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.about)) },
                    onClick = {
                        shouldShowDialog.value = true
                        expanded = false
                    },
                    modifier = Modifier.padding(8.dp)
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.logout)) },
                    onClick = {
                        logoutAction?.invoke()
                        expanded = false
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    )
}

@Composable
fun MyAlertDialog(shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = { shouldShowDialog.value = false },
            title = { Text(text = stringResource(R.string.about)) },
            text = { Text(text = stringResource(R.string.plaintext_password_manager_v1_0)) },
            confirmButton = {
                Button(onClick = { shouldShowDialog.value = false }) { Text(text = "Ok") }
            }
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark List")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light List")
@Composable
fun ListScreenPreview() {
    val mockState = ListViewState(
        passwordList = listOf(
            PasswordInfo(1, "Site Example", "user123", "strongPassword", "Notes here"),
            PasswordInfo(2, "App XYZ", "email@domain.com", "securePass123", "")
        ),
        isCollected = true
    )
    MaterialTheme {
        ListContent(listState = mockState, onItemClick = {})
    }
}

@Preview(showBackground = true, name = "Empty List")
@Composable
fun EmptyListPreview() {
    MaterialTheme {
        EmptyListScreen()
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun LoadingPreview() {
    MaterialTheme {
        LoadingScreen()
    }
}