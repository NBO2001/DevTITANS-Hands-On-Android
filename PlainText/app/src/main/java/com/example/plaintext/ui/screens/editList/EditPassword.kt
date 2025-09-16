package com.example.plaintext.ui.screens.editList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.plaintext.R
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.PlainTextAppState
import com.example.plaintext.ui.screens.Screen
import com.example.plaintext.ui.viewmodel.EditPasswordViewModel

data class EditListState(
    val nameState: String = "",
    val usernameState: String = "",
    val passwordState: String = "",
    val notesState: String = ""
)

@Composable
fun Edit_screen(
    args: Screen.EditList,
    appState: PlainTextAppState,
    viewModel: EditPasswordViewModel = hiltViewModel()
) {
    // Initialize EditListState with values from args
    val editListState = remember {
        EditListState(
            nameState = args.password.name,
            usernameState = args.password.login,
            passwordState = args.password.password,
            notesState = args.password.notes
        )
    }

    Scaffold(
        topBar = {
            TopBarComponent(
                title = if (args.password.id > 0) "Edit Password" else "Add Password",
                showBackButton = true,
                onBackClick = appState::back
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var name by remember { mutableStateOf(editListState.nameState) }
            var username by remember { mutableStateOf(editListState.usernameState) }
            var password by remember { mutableStateOf(editListState.passwordState) }
            var notes by remember { mutableStateOf(editListState.notesState) }

            EditInput(
                label = "Name",
                value = name,
                onValueChange = { name = it }
            )
            EditInput(
                label = "Username/Login",
                value = username,
                onValueChange = { username = it }
            )
            EditInput(
                label = "Password",
                value = password,
                onValueChange = { password = it }
            )
            EditInput(
                label = "Notes",
                value = notes,
                onValueChange = { notes = it },
                inputHeight = 120
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val passwordInfo = PasswordInfo(
                        id = args.password.id,
                        name = name,
                        login = username,
                        password = password,
                        notes = notes
                    )
                    viewModel.savePassword(passwordInfo)
                    appState.back()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun EditInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    inputHeight: Int = 60
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .height(inputHeight.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, // Replace with your back arrow drawable
                        contentDescription = "Back"
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun EditListPreview() {
    val nav = rememberNavController()
    val context = nav.context
    val state = PlainTextAppState(nav, context)
    val viewModel = hiltViewModel<EditPasswordViewModel>()
    Edit_screen(
        Screen.EditList(PasswordInfo(0, "Name", "Username", "Password", "Notes")),
        state,
        viewModel
    )
}

@Preview(showBackground = true)
@Composable
fun EditInputPreview() {
    EditInput(
        label = "Name",
        value = "",
        onValueChange = {}
    )
}