package com.example.plaintext.ui.screens.editList

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.screens.util.MyTopBar
import com.example.plaintext.ui.theme.*
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


enum class PasswordFormMode { CREATE, EDIT }

data class EditListState(
    val name: String = "",
    val user: String = "",
    val password: String = "",
    val notes: String = ""
)

fun isPasswordEmpty(password: PasswordInfo): Boolean {
    return  password.name.isEmpty() &&
            password.login.isEmpty() &&
            password.password.isEmpty() &&
            password.notes.isEmpty()
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditList(
    args: Screen.EditList,
    navigateBack: () -> Unit,
    savePassword: (password: PasswordInfo) -> Unit
) {
    PasswordForm(
        mode = PasswordFormMode.EDIT,
        initial = EditListState(
            name = args.password.name,
            user = args.password.login,
            password = args.password.password,
            notes = args.password.notes
        ),
        onBack = navigateBack,
        onSave = { updated -> savePassword(
            args.password.copy(
                name = updated.name,
                login = updated.user,
                password = updated.password,
                notes = updated.notes
            )
        )}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordForm(
    mode: PasswordFormMode,
    initial: EditListState = EditListState(),
    onBack: () -> Unit,
    onSave: (EditListState) -> Unit
){
    var name by rememberSaveable { mutableStateOf(initial.name) }
    var user by rememberSaveable { mutableStateOf(initial.user) }
    var password by rememberSaveable { mutableStateOf(initial.password) }
    var notes by rememberSaveable { mutableStateOf(initial.notes) }

    val title = when (mode) {
        PasswordFormMode.CREATE -> "Criar nova senha"
        PasswordFormMode.EDIT -> "Editar senha"
    }

    Scaffold(
        topBar = { MyTopBar(title = title, onBack = onBack) }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditInput(
                textInputLabel = "Nome",
                value = name,
                onValueChange = { name = it },
                imeAction = ImeAction.Next
            )

            EditInput(
                textInputLabel = "Usuário",
                value = user,
                onValueChange = { user = it },
                imeAction = ImeAction.Next
            )

            EditInput(
                textInputLabel = "Senha",
                value = password,
                onValueChange = { password = it },
                keyboardType = "password",
                imeAction = ImeAction.Next
            )

            EditInput(
                textInputLabel = "Notas",
                value = notes,
                onValueChange = { notes = it },
                textInputHeight = 150,
                keyboardType = "text",
                imeAction = ImeAction.Done
            )

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onSave(EditListState(name, user, password, notes)) },
                colors = ButtonDefaults.buttonColors( containerColor = Lime)
            ) {
                Text("Salvar")
            }

        }
    }
}


@Composable
fun EditInput(
    textInputLabel: String,
    value: String,
    onValueChange: (String) -> Unit,
    textInputHeight: Int = 60,
    keyboardType: String = "text",
    imeAction: ImeAction = ImeAction.Default
) {
    val padding = 30
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val isPasswordField = keyboardType == "password"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(textInputHeight.dp)
            .padding(horizontal = padding.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(textInputLabel) },
            modifier = Modifier
                .height(textInputHeight.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = when (keyboardType) {
                    "text" -> KeyboardType.Text
                    "number" -> KeyboardType.Number
                    "password" -> KeyboardType.Password
                    else -> KeyboardType.Text
                },
                imeAction = imeAction
            ),
            visualTransformation = if (isPasswordField && !showPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                if (isPasswordField) {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector =
                                if (showPassword) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                            contentDescription = if (showPassword) "Ocultar senha" else "Mostrar senha"
                        )
                    }
                }
            }
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}


/* ===========================
   PREVIEWS
   =========================== */

@Preview(showBackground = true, backgroundColor = 0xFF261307)
@Composable
fun AddPasswordPreview() {
    PasswordForm(
        mode = PasswordFormMode.CREATE,
        onBack = {},
        onSave = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF261307)
@Composable
fun EditPasswordPreview() {
    PasswordForm(
        mode = PasswordFormMode.EDIT,
        initial = EditListState(
            name = "Facebook",
            user = "devtitans",
            password = "123",
            notes = "Bla bla"
        ),
        onBack = {},
        onSave = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EditListPreview() {
    EditList(
        Screen.EditList(PasswordInfo(1, "Nome", "Usuário", "Senha", "Notas")),
        navigateBack = {},
        savePassword = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EditInputPreview() {
    EditInput("Nome", "Usuário", {})
}