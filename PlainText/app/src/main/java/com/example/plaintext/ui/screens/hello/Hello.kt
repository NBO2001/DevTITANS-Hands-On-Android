package com.example.plaintext.ui.screens.hello

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.repository.PasswordDBStore
import com.example.plaintext.ui.screens.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListViewState(
    var listState: List<PasswordInfo> = emptyList(),
    var size: Int = 0
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {
    var listState by mutableStateOf(ListViewState())
        private set
    private var dataList = mutableListOf<PasswordInfo>()

    init {
        viewModelScope.launch {
            if (passwordDBStore.isEmpty().stateIn(viewModelScope).value) {
                for (i in 1..100) {
                    val p = PasswordInfo(
                        i,
                        "devtitans #$i",
                        "devtitans #$i",
                        "devtitans #$i",
                        "devtitans #$i"
                    )
                    Log.d("ListViewModel", "Password: $p")
                    passwordDBStore.add(p.toPassword())
                }
            }
            dataList = passwordDBStore.getList().map { list ->
                list.map { it.toPasswordInfo() }
            }.stateIn(viewModelScope).value as MutableList<PasswordInfo>

            dataList.forEach {
                Log.d("ListViewModel", "Checking Password: $it")
            }

            collectData()
        }
    }

    fun collectData() {
        viewModelScope.launch {
            Log.d("ListViewModel", "Collecting data")
            delay(5000)
            listState = ListViewState(dataList, dataList.size)
            listState.listState.forEach {
                Log.d("ListViewModel", "ListState Password: $it")
            }
        }
    }
}

@Composable
fun Hello_screen(modifier: Modifier, viewModel: ListViewModel = hiltViewModel()) {
    val listViewState: ListViewState = viewModel.listState

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (listViewState.listState.isEmpty()) {
            Text("Carregando...", fontSize = 20.sp)
        } else {
            Column() {
                Text(
                    text = "Total de itens: ${listViewState.size}",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .padding(16.dp)
                )
                LazyColumn {
                    items(listViewState.listState.size) { index ->
                        Text(
                            text = getPasswordAsText(listViewState, index),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                    }
                }

            }

        }
    }
}

private fun getPasswordAsText(
    listViewState: ListViewState,
    index: Int
): String {
    val pi = listViewState.listState[index]
    val sb = StringBuilder()
    sb.append("ID: ${pi.id}\n")
    sb.append("Nome: ${pi.name}\n")
    sb.append("Login: ${pi.login}\n")
    sb.append("Pass: ${pi.password}\n")
    sb.append("Notes: ${pi.notes}\n")
    return sb.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hello_screen(args: Screen.Hello) {
    Scaffold { padding ->
        Hello_screen(Modifier.padding(padding))
    }
}
