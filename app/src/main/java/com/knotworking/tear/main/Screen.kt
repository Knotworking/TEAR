package com.knotworking.tear.main

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object SettingsScreen : Screen("settings_screen")
}
