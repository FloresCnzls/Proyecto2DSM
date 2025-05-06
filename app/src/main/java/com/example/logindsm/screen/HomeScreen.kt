package com.example.logindsm.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val currentUser = Firebase.auth.currentUser
    val userEmail = currentUser?.email ?: "Usuario desconocido"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla de inicio")
        Text(text = "BIENVENIDO: $userEmail")

        Button(onClick = {
            Firebase.auth.signOut()
            navHostController.navigate("auth") {
                popUpTo("home") { inclusive = true }
            }
        }) {
            Text(text = "Cerrar sesión")
        }
    }
}