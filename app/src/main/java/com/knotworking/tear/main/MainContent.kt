package com.knotworking.tear.main

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
internal fun LocationContent(navController: NavController, viewModel: LocationViewModel) {
    val locationViewState by viewModel.locationViewState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = { navController.navigate(Screen.SettingsScreen.route) },
            modifier = Modifier.align (Alignment.TopEnd)
        ) {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${locationViewState.kmProgress?.toInt()}km")
            Text("${String.format("%.2f", locationViewState.percentageProgress)}%")
            Text(
                "${
                    String.format(
                        "%.2f",
                        locationViewState.distanceToTrail?.div(1000)
                    )
                }km to trail"
            )
            Text(
                text = "${
                    locationViewState.latitude?.toString()?.plus(" lat, ") ?: ""
                }${"${locationViewState.longitude?.toString()} lon" ?: ""}"
            )
            Spacer(modifier = Modifier.height(16.dp))
            LocationButton(viewModel = viewModel, locationViewState = locationViewState)
        }
    }
}

@Composable
internal fun LocationButton(
    viewModel: LocationViewModel,
    locationViewState: LocationViewModel.LocationViewState
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("TAG", "PERMISSION GRANTED")
            viewModel.startLocationUpdates()
        } else {
            // Permission Denied: Do something
            Log.d("TAG", "PERMISSION DENIED")
        }
    }

    Button(onClick = {
        if (locationViewState.receivingUpdates) {
            viewModel.stopLocationUpdates()
        } else {
            startLocationUpdates(context, viewModel, launcher)
        }
    }) {
        when {
            locationViewState.loading -> {
                CircularProgressIndicator(color = Color.White)
            }
            locationViewState.receivingUpdates -> {
                Text(text = "stop")
            }
            else -> {
                Text(text = "start")
            }
        }
    }
}

private fun startLocationUpdates(
    context: Context,
    viewModel: LocationViewModel,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    when (context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
        true -> {
            // Some work that requires permission
            Log.d("TAG", "Code requires permission")
            viewModel.startLocationUpdates()
        }
        else -> {
            // Asking for permission
            Log.d("TAG", "Ask for permission")
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}