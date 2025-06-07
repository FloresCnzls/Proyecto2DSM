package com.example.logindsm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logindsm.screen.AllEventsScreen
import com.example.logindsm.screen.AuthScreen
import com.example.logindsm.screen.CreateEventScreen
import com.example.logindsm.screen.EditEventScreen
import com.example.logindsm.screen.EventListParticipatingScreen
import com.example.logindsm.screen.HomeScreen
import com.example.logindsm.screen.LoginScreen
import com.example.logindsm.screen.MyCreatedEventsScreen
import com.example.logindsm.screen.SignupScreen
import com.example.logindsm.screen.WelcomeScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()

    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage = if(isLoggedIn) "home" else "auth"

    NavHost(navController = navController, startDestination = firstPage){

        composable("auth"){
            AuthScreen(modifier, navController)
        }
        composable("login"){
            LoginScreen(modifier, navController)
        }
        composable("signup"){
            SignupScreen(modifier, navController)
        }
        composable("home"){
            HomeScreen(modifier, navController)
        }
        composable("create_event") {
            CreateEventScreen(navHostController = navController)
        }
        composable("event_list_participating") {
            EventListParticipatingScreen(navHostController = navController)
        }
        composable("my_created_events") {
            MyCreatedEventsScreen(navHostController = navController)
        }
        composable("all_events") {
            AllEventsScreen(navHostController = navController)
        }
        composable(
            route = "edit_event/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EditEventScreen(eventId = eventId, navHostController = navController)
        }
    }
}