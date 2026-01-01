package com.example.blooddonor.ui.navigations

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Search : Screen("search")
    object Emergency : Screen("emergency")
    object Profile : Screen("profile")
    object DonationHistory : Screen("donation_history")
    object RequestHistory : Screen("request_history")
}