package com.example.logindsm.screen

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, email: String, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido, $email!", fontSize = 26.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de Cerrar sesión
        Button(
            onClick = {
                navController.popBackStack() // Regresa a la pantalla anterior
            },
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text(text = "Cerrar Sesión", fontSize = 22.sp)
        }
    }
}