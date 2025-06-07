package com.example.logindsm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logindsm.model.EventModel
import com.example.logindsm.model.EventModelWithId
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun MyCreatedEventsScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val firestore = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    var events by remember { mutableStateOf(listOf<EventModelWithId>()) }
    var message by remember { mutableStateOf<String?>(null) }

    // Cargar eventos creados por el usuario
    LaunchedEffect(Unit) {
        currentUser?.uid?.let { uid ->
            firestore.collection("events")
                .whereEqualTo("creatorId", uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        message = "Error al cargar eventos: ${error.message}"
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val list = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(EventModel::class.java)?.let { event ->
                                EventModelWithId.from(doc.id, event)
                            }
                        }
                        events = list
                    }
                }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Mis eventos creados", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (events.isEmpty()) {
            Text("No tienes eventos creados.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(events) { event ->
                    EventItem(
                        event = event,
                        onDelete = {
                            firestore.collection("events").document(event.id)
                                .delete()
                                .addOnSuccessListener {
                                    message = "Evento eliminado correctamente"
                                }
                                .addOnFailureListener {
                                    message = "Error al eliminar evento: ${it.message}"
                                }
                        },
                        onEdit = {
                            // Aquí navegas a una pantalla de edición y pasas el id
                            navHostController.navigate("edit_event/${event.id}")
                        }
                    )
                }
            }
        }

        message?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun EventItem(event: EventModelWithId, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Descripción: ${event.description}")
            Text(text = "Fecha: ${event.date}")
            Text(text = "Ubicación: ${event.location}")

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = onEdit, modifier = Modifier.weight(1f)) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Eliminar")
                }
            }
        }
    }
}
