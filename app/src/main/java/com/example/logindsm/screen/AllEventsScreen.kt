package com.example.logindsm.screen

import androidx.compose.foundation.clickable
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase as KtxFirebase

@Composable
fun AllEventsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val firestore = KtxFirebase.firestore
    val auth = Firebase.auth
    val currentUserId = auth.currentUser?.uid ?: ""

    var events by remember { mutableStateOf(listOf<EventModelWithId>()) }
    var isLoading by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        firestore.collection("events")
            .whereNotEqualTo("creatorId", currentUserId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val list = querySnapshot.documents.mapNotNull { doc ->
                    val event = doc.toObject(EventModel::class.java)
                    event?.let { EventModelWithId.from(doc.id, it) }
                }
                events = list
                isLoading = false
            }
            .addOnFailureListener {
                message = "Error al cargar eventos: ${it.message}"
                isLoading = false
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Eventos disponibles", style = MaterialTheme.typography.headlineMedium)

        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        } else if (events.isEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("No hay eventos disponibles.")
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(events) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = event.description)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Fecha: ${event.date}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Ubicación: ${event.location}")
                            Spacer(modifier = Modifier.height(8.dp))

                            val isAttending = event.participants.contains(currentUserId)

                            Button(
                                onClick = {
                                    if (!isAttending) {
                                        // Agregar usuario a la lista de participantes en Firestore
                                        val eventRef = firestore.collection("events").document(event.id)
                                        eventRef.update("participants", event.participants + currentUserId)
                                            .addOnSuccessListener {
                                                message = "Asistencia confirmada para: ${event.title}"
                                                // Actualizamos localmente para reflejar el cambio sin recargar
                                                events = events.map {
                                                    if (it.id == event.id) it.copy(
                                                        participants = it.participants + currentUserId
                                                    ) else it
                                                }
                                            }
                                            .addOnFailureListener {
                                                message = "Error al confirmar asistencia: ${it.message}"
                                            }
                                    } else {
                                        message = "Ya estás registrado en este evento."
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isAttending
                            ) {
                                Text(if (isAttending) "Ya Asistes" else "Asistir")
                            }
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