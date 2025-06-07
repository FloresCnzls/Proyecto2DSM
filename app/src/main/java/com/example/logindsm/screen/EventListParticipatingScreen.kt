package com.example.logindsm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logindsm.model.EventModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.logindsm.model.EventModelWithId
@Composable
fun EventListParticipatingScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = Firebase.auth.currentUser
    var userEvents by remember { mutableStateOf<List<EventModelWithId>>(emptyList()) }
    var message by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        val uid = currentUser?.uid ?: return@LaunchedEffect
        db.collection("events")
            .whereArrayContains("participants", uid)
            .get()
            .addOnSuccessListener { result ->
                val eventsWithId = result.documents.mapNotNull { doc ->
                    val event = doc.toObject(EventModelWithId::class.java)
                    if (event != null) {
                        event.copy(id = doc.id)
                    } else null
                }
                userEvents = eventsWithId
            }
            .addOnFailureListener {
                message = "Error cargando eventos: ${it.message}"
            }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Eventos donde participas", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (userEvents.isEmpty()) {
            Text("No estás participando en ningún evento.")
        } else {
            userEvents.forEach { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Título: ${event.title}")
                        Text("Fecha: ${event.date}")
                        Text("Ubicación: ${event.location}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                // Eliminar al usuario de la lista de participantes
                                val uid = currentUser?.uid ?: return@Button
                                val eventRef = db.collection("events").document(event.id)

                                val updatedParticipants = event.participants.toMutableList()
                                updatedParticipants.remove(uid)

                                eventRef.update("participants", updatedParticipants)
                                    .addOnSuccessListener {
                                        message = "Has cancelado tu asistencia a: ${event.title}"
                                        userEvents = userEvents.filter { it.id != event.id }
                                    }
                                    .addOnFailureListener {
                                        message = "Error al cancelar asistencia: ${it.message}"
                                    }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancelar asistencia")
                        }
                    }
                }
            }
        }

        message?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
    }
}