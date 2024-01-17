package com.emmutua.vehiclehire.presentation.auth

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AuthNavGraph(
    startDestination: String,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    activity: Activity
) {
    NavHost(navController = navController, startDestination = startDestination) {
        loginScreen(
            viewModel = authViewModel,
            onGotoSignUpClicked = {
                navController.navigateWithPop(AuthScreen.Register.route)
            },
            navigateToHome = {
                navController.navigate(AuthScreen.Home.route) {
                    navController.popBackStack()
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
        )
        registerScreen(
            viewModel = authViewModel,
            navigateToLogin = {
                navController.navigateWithPop(AuthScreen.Login.route)
            },
            navigateBack = {
                navController.popBackStack()
            }
        )
        homeScreen(
            logOut = {
                authViewModel.signOut()
                activity.finish()
            }
        )
    }
}

fun NavGraphBuilder.loginScreen(
    viewModel: AuthViewModel,
    onGotoSignUpClicked: () -> Unit,
    navigateToHome: () -> Unit,
) {
    composable(AuthScreen.Login.route) {
        LoginScreen(
            viewModel = viewModel,
            onGotoSignUpClicked = onGotoSignUpClicked,
            navigateToHome = navigateToHome,
        )
    }
}

fun NavGraphBuilder.registerScreen(
    viewModel: AuthViewModel,
    navigateToLogin: () -> Unit,
    navigateBack: () -> Unit,
) {
    composable(AuthScreen.Register.route) {
        RegisterScreen(
            viewModel = viewModel,
            navigateBack = navigateBack,
            onSuccessRegistration = {
                navigateToLogin()
            },
            onGotoLoginClicked = {
                navigateToLogin()
            },
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
fun NavGraphBuilder.homeScreen(
    logOut: () -> Unit
) {
    composable(AuthScreen.Home.route) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextButton(onClick = logOut) {
                Text(text = "Log Out")
            }
        }
    }
}

fun NavController.navigateWithPop(route: String) {
    navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
