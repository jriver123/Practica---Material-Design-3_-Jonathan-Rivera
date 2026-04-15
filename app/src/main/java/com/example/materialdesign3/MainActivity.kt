package com.example.materialdesign3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MaterialDesign3)
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                PantallaListaTareas()
            }
        }
    }

    }
data class Tarea(
    val nombre: String,
    val descripcion: String,
    val estado: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaTareas() {
    var textoTarea by remember { mutableStateOf("") }
    var descripcionTarea by remember { mutableStateOf("") }
    var estadoSeleccionado by remember { mutableStateOf("En espera") }
    var expanded by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("Agrega una tarea con su descripción y estado") }

    val estados = listOf("En espera", "En proceso", "Finalizado")
    val listaTareas = remember { mutableStateListOf<Tarea>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de Tareas",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Nueva tarea",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = textoTarea,
                        onValueChange = { textoTarea = it },
                        label = { Text("Nombre de la tarea") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = descripcionTarea,
                        onValueChange = { descripcionTarea = it },
                        label = { Text("Descripción de la tarea") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = estadoSeleccionado,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Estado") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            estados.forEach { estado ->
                                DropdownMenuItem(
                                    text = { Text(estado) },
                                    onClick = {
                                        estadoSeleccionado = estado
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (textoTarea.isNotBlank() && descripcionTarea.isNotBlank()) {
                                listaTareas.add(
                                    Tarea(
                                        nombre = textoTarea,
                                        descripcion = descripcionTarea,
                                        estado = estadoSeleccionado
                                    )
                                )
                                mensaje = "Tarea agregada en estado: $estadoSeleccionado"
                                textoTarea = ""
                                descripcionTarea = ""
                                estadoSeleccionado = "En espera"
                            } else {
                                mensaje = "Debes escribir el nombre y la descripción de la tarea"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Agregar tarea")
                    }

                    Button(
                        onClick = {
                            val cantidadAntes = listaTareas.size
                            listaTareas.removeAll { it.estado == "Finalizado" }
                            val eliminadas = cantidadAntes - listaTareas.size

                            mensaje = if (eliminadas > 0) {
                                "Se eliminaron $eliminadas tarea(s) finalizada(s)"
                            } else {
                                "No hay tareas finalizadas para borrar"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Borrar tareas finalizadas")
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        tonalElevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = mensaje,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Text(
                text = "Tareas registradas",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(listaTareas) { tarea ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = tarea.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Descripción: ${tarea.descripcion}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Estado: ${tarea.estado}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = when (tarea.estado) {
                                    "En espera" -> MaterialTheme.colorScheme.tertiary
                                    "En proceso" -> MaterialTheme.colorScheme.primary
                                    "Finalizado" -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaListaTareas() {
    MaterialTheme {
        PantallaListaTareas()
    }
}