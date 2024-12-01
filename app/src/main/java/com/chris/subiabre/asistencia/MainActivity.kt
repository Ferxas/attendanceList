package com.chris.subiabre.asistencia

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chris.subiabre.asistencia.ui.theme.Chris_Subiabre_20241129_seccioncursoTheme

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)

        setContent {
            Chris_Subiabre_20241129_seccioncursoTheme {
                AppContent(dbHelper)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(dbHelper: DatabaseHelper) {
    val context = LocalContext.current // Obtener el contexto composable
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Entrada") }
    val tipos = listOf("Entrada", "Salida")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Control de Asistencia") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campos de entrada
            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (YYYY-MM-DD)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (HH:MM)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Menú desplegable
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    tipos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) }, // Cambiado: Material3 usa el parámetro `text`
                            onClick = {
                                tipo = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Botón para guardar asistencia
            Button(
                onClick = {
                    val id = dbHelper.insertarAsistencia(rut, nombre, apellido, fecha, hora, tipo)
                    if (id > 0) {
                        Toast.makeText(
                            context, // Contexto válido
                            "Asistencia registrada con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Error al registrar asistencia",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Asistencia")
            }

            // Botón para listar asistencias
            Button(
                onClick = {
                    val asistencias = dbHelper.obtenerAsistencias()
                    val lista = asistencias.joinToString("\n") {
                        "${it[DatabaseHelper.COLUMN_RUT]} - ${it[DatabaseHelper.COLUMN_FECHA]} - ${it[DatabaseHelper.COLUMN_HORA]} (${it[DatabaseHelper.COLUMN_TIPO]})"
                    }
                    Toast.makeText(
                        context, // Contexto válido
                        lista,
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Listar Asistencias")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    Chris_Subiabre_20241129_seccioncursoTheme {
        AppContent(DatabaseHelper(LocalContext.current))
    }
}
