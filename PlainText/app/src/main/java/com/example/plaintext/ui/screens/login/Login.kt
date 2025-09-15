package com.example.plaintext.ui.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize // Adicionado para ocupar a tela inteira
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview // Importante para o Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plaintext.R
import com.example.plaintext.ui.viewmodel.LoginViewModel // Importe o LoginViewModel
import com.example.plaintext.ui.viewmodel.PreferencesViewModel

// Removida a data class LoginState pois o ViewModel vai gerenciar isso

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login_screen(
    navigateToSettings: () -> Unit,
    navigateToList: (name: String) -> Unit, // Alterado para receber o nome
    viewModel: LoginViewModel = hiltViewModel() // Injetando o LoginViewModel
) {
    val context = LocalContext.current
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) } // Estado para mensagem de erro

    Scaffold(
        topBar = {
            TopBarComponent( // Reutilizando seu TopBarComponent existente
                navigateToSettings = navigateToSettings,
                navigateToSensores = { /* Não usado aqui, mas manter para compatibilidade */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // Permite rolagem se o conteúdo for grande
                .background(MaterialTheme.colorScheme.background), // Usando a cor de fundo do tema
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Parte superior (verde escuro na imagem)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Altura fixa para a parte superior
                    .background(Color(0xFF4CAF50)) // Cor verde similar à imagem (Material Green 500)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Substitua pelo seu ícone do Android
                    contentDescription = "Android Icon",
                    modifier = Modifier.width(70.dp).height(70.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name), // Você pode definir o nome do app em strings.xml
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "O mais seguro\ngerenciador de senhas\nBob and Alice",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }

            // Parte de baixo (formulário de login)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaço entre os elementos
            ) {
                Text(
                    text = "Digite suas credenciais para continuar",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo de Login
                OutlinedTextField(
                    value = viewModel.loginText,
                    onValueChange = viewModel::onLoginTextChange,
                    label = { Text("Login") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null // Indica erro se houver mensagem
                )

                // Campo de Senha
                OutlinedTextField(
                    value = viewModel.passwordText,
                    onValueChange = viewModel::onPasswordTextChange,
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation(), // Para esconder a senha
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null // Indica erro se houver mensagem
                )

                // Exibir mensagem de erro, se houver
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start // Alinhar texto de erro à esquerda
                    )
                }

                // Checkbox "Salvar informações de login"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = viewModel.rememberMe,
                        onCheckedChange = viewModel::onRememberMeChange
                    )
                    Text(text = "Salvar as informações de login")
                }

                Spacer(modifier = Modifier.height(24.dp)) // Espaço antes do botão

                // Botão "Enviar"
                Button(
                    onClick = {
                        errorMessage = null // Limpa erro anterior antes de tentar login
                        viewModel.onLoginClick(
                            onSuccess = {
                                Log.d("LoginScreen", "Login bem-sucedido!")
                                navigateToList(viewModel.loginText) // Navega com o nome de login
                            },
                            onError = { message ->
                                errorMessage = message // Define a mensagem de erro
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp) // Altura mínima para o botão
                ) {
                    Text(text = "Enviar", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun MyAlertDialog(shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },

            title = { Text(text = "Sobre") },
            text = { Text(text = "PlainText Password Manager v1.0") },
            confirmButton = {
                Button(
                    onClick = { shouldShowDialog.value = false }
                ) {
                    Text(text = "Ok")
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarComponent(
    navigateToSettings: (() -> Unit?)? = null,
    navigateToSensores: (() -> Unit?)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val shouldShowDialog = remember { mutableStateOf(false) }

    if (shouldShowDialog.value) {
        MyAlertDialog(shouldShowDialog = shouldShowDialog)
    }

    TopAppBar(
        title = { Text("PlainText") },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Configurações") },
                    onClick = {
                        navigateToSettings?.invoke(); // Chamar se não for nulo
                        expanded = false;
                    },
                    modifier = Modifier.padding(8.dp)
                )
                DropdownMenuItem(
                    text = {
                        Text("Sobre");
                    },
                    onClick = {
                        shouldShowDialog.value = true;
                        expanded = false;
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    )
}

// Função de Preview para a Login_screen
@Preview(showBackground = true, widthDp = 360)
@Composable
fun LoginScreenPreview() {
    // Para o Preview, podemos passar lambdas vazios ou mockados
    Login_screen(
        navigateToSettings = { Log.d("Preview", "Navegar para Configurações (Preview)") },
        navigateToList = { name -> Log.d("Preview", "Navegar para Lista com nome: $name (Preview)") },
        viewModel = LoginViewModel() // Instância simples para o preview
    )
}