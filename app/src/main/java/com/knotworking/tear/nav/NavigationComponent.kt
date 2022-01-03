package com.knotworking.tear.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.knotworking.tear.main.LocationContentWrapper
import com.knotworking.tear.main.LocationViewModel
import com.knotworking.tear.settings.SettingsContent
import com.knotworking.tear.settings.SettingsViewModel
import org.koin.androidx.compose.viewModel

@Composable
fun NavigationComponent() {
    val locationViewModel: LocationViewModel by viewModel()
    val settingsViewModel: SettingsViewModel by viewModel()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            LocationContentWrapper(
                openSettings = { navController.navigate(Screen.SettingsScreen.route) },
                viewModel = locationViewModel
            )
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsContent(
                viewModel = settingsViewModel,
                onClose = { navController.popBackStack() })
        }
    }
}

