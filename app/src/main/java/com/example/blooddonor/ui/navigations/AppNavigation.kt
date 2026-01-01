package com.example.blooddonor.ui.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.blooddonor.ui.screens.DashboardScreen
import com.example.blooddonor.ui.screens.DonationHistoryScreen
import com.example.blooddonor.ui.screens.EmergencyScreen
import com.example.blooddonor.ui.screens.LoginScreen
import com.example.blooddonor.ui.screens.ProfileScreen
import com.example.blooddonor.ui.screens.RegisterScreen
import com.example.blooddonor.ui.screens.RequestHistoryScreen
import com.example.blooddonor.ui.screens.SearchScreen
import com.example.blooddonor.ui.screens.WelcomeScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(Screen.Search.route) {
            SearchScreen(navController)
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(Screen.Emergency.route) {
            EmergencyScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(Screen.DonationHistory.route) {
            DonationHistoryScreen(navController)
        }

        composable(Screen.RequestHistory.route) {
            RequestHistoryScreen(navController)
        }
    }
}