package com.dev.carrenting.presentation.auth

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "login")
    object Register : AuthScreen(route = "register")
    object Home : AuthScreen(route = "home")
}
