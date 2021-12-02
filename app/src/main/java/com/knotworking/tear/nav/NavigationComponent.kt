package com.knotworking.tear.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.knotworking.tear.main.LocationContent
import com.knotworking.tear.main.LocationViewModel
import com.knotworking.tear.main.SettingsContent
import org.koin.androidx.compose.viewModel

@Composable
fun NavigationComponent() {
    val locationViewModel: LocationViewModel by viewModel()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            LocationContent(navController = navController, viewModel = locationViewModel)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsContent()
        }
    }
}
