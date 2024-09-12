package com.example.navegacao1.ui.telas

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.Endereco
import com.example.navegacao1.model.dados.RetrofitClient
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TelaPrincipal(modifier: Modifier = Modifier, onLogoffClick: () -> Unit) {
    var scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Text(text = "Tela Principal")
        var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
        var endereco by remember { mutableStateOf<Endereco>(Endereco()) }
        var novoUsuario by remember { mutableStateOf(Usuario()) }
        var usuarioBuscado by remember { mutableStateOf<Usuario?>(null) }
        var usuarioId by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            scope.launch {
                Log.d("principal", "Carregando usuários")
                usuarios = getUsuarios()
                // endereco = getEndereco()
            }
        }

        Text(endereco.logradouro)

        Button(onClick = {
            scope.launch {
                val usuarioInserido = inserirUsuario(novoUsuario)
                Log.d("principal", "Usuário inserido: ${usuarioInserido.nome}")
                usuarios = getUsuarios()
            }
        }) {
            Text("Inserir Usuário")
        }

        OutlinedTextField(
            value = usuarioId,
            onValueChange = { usuarioId = it },
            label = { Text("Digite o ID do usuário") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(onClick = {
            scope.launch {
                try {
                    val id = usuarioId.toInt()
                    usuarioBuscado = buscarUsuarioPorId(id)
                    Log.d("principal", "Usuário encontrado: ${usuarioBuscado?.nome}")
                } catch (e: Exception) {
                    Log.e("principal", "Erro ao buscar usuário: ${e.message}")
                }
            }
        }) {
            Text("Buscar Usuário por ID")
        }

        usuarioBuscado?.let { usuario ->
            Text(text = "Usuário buscado: ${usuario.nome}", modifier = Modifier.padding(8.dp))
        }

        Button(onClick = {
            scope.launch {
                removerUsuario(1)
                Log.d("principal", "Usuário removido")
                usuarios = getUsuarios()
            }
        }) {
            Text("Remover Usuário")
        }

        Button(onClick = { onLogoffClick() }) {
            Text("Sair")
        }

        LazyColumn {
            items(usuarios) { usuario ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = usuario.nome)
                    }
                }
            }
        }
    }
}

suspend fun getUsuarios(): List<Usuario> {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.listar()
    }
}

suspend fun inserirUsuario(usuario: Usuario): Usuario {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.inserir(usuario)
    }
}

suspend fun removerUsuario(id: Int) {
    withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.remover(id)
    }
}

suspend fun buscarUsuarioPorId(id: Int): Usuario {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.buscarPorId(id)
    }
}
