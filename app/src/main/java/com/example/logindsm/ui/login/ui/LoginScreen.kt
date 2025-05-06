package com.example.logindsm.ui.login.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.logindsm.R

@Composable
fun LoginScreen(){
    Box(Modifier.fillMaxSize().padding(16.dp)){
        Login(Modifier.align(Alignment.Center))
    }
}

@Composable
fun Login(modifier: Modifier) {
    Column (modifier= Modifier){
    HeaderImage()
        Spacer(modifier = Modifier.padding(16.dp) )
    }
}

@Composable
fun HeaderImage(){
    Image(painter =  painterResource(id= R.drawable.udb), contentDescription = "HEADER")
}