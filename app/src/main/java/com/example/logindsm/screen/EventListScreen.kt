package com.example.logindsm.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logindsm.model.EventModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EventListScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val firestore = FirebaseFirestore.getInstance()
    var events by remember { mutableStateOf<List<Pair<String, EventModel>>>(emptyList()) }

    LaunchedEffect(Unit) {
        firestore.collection("events").get()
            .addOnSuccessListener { result ->
                events = result.documents.mapNotNull {
                    val model = it.toObject(EventModel::class.java)
                    if (model != null) it.id to model else null
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Eventos disponibles", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        events.forEach { (id, event) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        // Puedes agregar navegación a detalle si lo deseas
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Título: ${event.title}", style = MaterialTheme.typography.titleMedium)
                    Text("Fecha: ${event.date}")
                    Text("Ubicación: ${event.location}")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navHostController.navigate("create_event") }) {
            Text("Crear nuevo evento")
        }
    }
}