package com.example.logindsm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.logindsm.AppUtil
import com.example.logindsm.R
import com.example.logindsm.viewmodel.AuthViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier,navHostController: NavHostController, authViewModel : AuthViewModel = viewModel()){
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var context = LocalContext.current


    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(
            painter = painterResource(id = R.drawable.udb),
            contentDescription="Login BANNER",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text= "¡Hola de nuevo!",
            modifier= Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text= "Inicia sesion en tu cuenta",
            modifier= Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 22.sp,

                )
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Correo Electronico")
            },
            modifier = modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Ingrese su contraseña")
            },
            modifier = modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            isLoading = true
            authViewModel.login(email,password){success,errorMessage->
                if(success){
                    isLoading = false
                    navHostController.navigate("home") {
                        popUpTo("auth"){inclusive = true}
                    }
                }else{
                    isLoading = false
                    AppUtil.showToast(context, errorMessage?:"Algo salio mal")
                }
            }
        },


            enabled = !isLoading,
            modifier= Modifier.fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text =if(isLoading)"Iniciando Sesion" else "Iniciar Sesion", fontSize = 22.sp)
        }

    }
}