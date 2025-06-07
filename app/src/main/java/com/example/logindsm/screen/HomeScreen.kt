package com.example.logindsm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val currentUser = Firebase.auth.currentUser
    val userEmail = currentUser?.email ?: "Usuario desconocido"

    // Fondo degradado
    val gradient = Brush.verticalGradient(
        colors = listOf(colorScheme.primaryContainer, colorScheme.secondaryContainer)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Pantalla de inicio",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "BIENVENIDO: $userEmail",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = colorScheme.onPrimaryContainer
            )

            Spacer(Modifier.height(32.dp))

            // Botones con Card para dar profundidad y estilo
            @Composable
            fun MenuButton(text: String, onClick: () -> Unit) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.primary,
                    ),
                    elevation = CardDefaults.cardElevation(8.dp),
                ) {
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary,
                        ),
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = text, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            MenuButton("Crear nuevo evento") {
                navHostController.navigate("create_event")
            }

            MenuButton("Mis eventos creados") {
                navHostController.navigate("my_created_events")
            }

            MenuButton("Mis eventos (participando)") {
                navHostController.navigate("event_list_participating")
            }

            MenuButton("Ver todos los eventos") {
                navHostController.navigate("all_events")
            }

            Spacer(Modifier.weight(1f)) // Empuja el botón cerrar sesión hacia abajo

            MenuButton("Cerrar sesión") {
                Firebase.auth.signOut()
                navHostController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }
}
