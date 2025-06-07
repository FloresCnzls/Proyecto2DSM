package com.example.logindsm.screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavBackStackEntry
import com.example.logindsm.model.EventModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun EditEventScreen(
    eventId: String,
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val firestore = Firebase.firestore

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var updateSuccess by remember { mutableStateOf(false) }

    // Carga del evento cuando se abre la pantalla
    LaunchedEffect(eventId) {
        firestore.collection("events").document(eventId).get()
            .addOnSuccessListener { doc ->
                val event = doc.toObject(EventModel::class.java)
                if (event != null) {
                    title = event.title
                    description = event.description
                    date = event.date
                    location = event.location
                }
            }
            .addOnFailureListener {
                message = "Error al cargar evento: ${it.message}"
            }
    }

    // Navegación segura al detectar éxito en actualización
    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            navHostController.navigate("my_events") {
                popUpTo("edit_event/$eventId") { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text("Editar Evento", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Fecha") })
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Ubicación") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            firestore.collection("events").document(eventId)
                .update(
                    mapOf(
                        "title" to title,
                        "description" to description,
                        "date" to date,
                        "location" to location
                    )
                )
                .addOnSuccessListener {
                    updateSuccess = true
                }
                .addOnFailureListener {
                    message = "Error al actualizar evento: ${it.message}"
                }
        }) {
            Text("Actualizar evento")
        }

        message?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
