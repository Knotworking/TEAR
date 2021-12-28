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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.knotworking.tear.nav.Screen

@Composable
internal fun LocationContent(navController: NavController, viewModel: LocationViewModel) {
    val locationViewState by viewModel.locationViewState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    Snackbar(
        viewModel = viewModel,
        locationViewState = locationViewState,
        scaffoldState = scaffoldState
    )
    Scaffold(scaffoldState = scaffoldState) {
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = { navController.navigate(Screen.SettingsScreen.route) },
                modifier = Modifier.align(Alignment.TopEnd)
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
                    }${"${locationViewState.longitude?.toString()} lon"}"
                )
                Spacer(modifier = Modifier.height(16.dp))
                GetLocationButton(viewModel = viewModel, locationViewState = locationViewState)
                Spacer(modifier = Modifier.height(16.dp))
                UpdateLocationButton(viewModel = viewModel, locationViewState = locationViewState)
            }
        }
    }
}

@Composable
internal fun Snackbar(
    viewModel: LocationViewModel,
    locationViewState: LocationViewModel.LocationViewState,
    scaffoldState: ScaffoldState
) {
    LaunchedEffect(locationViewState) {
        if (locationViewState.snackbarText != null) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = locationViewState.snackbarText,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    viewModel.hideSnackbar()
                }
                SnackbarResult.Dismissed -> {
                    viewModel.hideSnackbar()
                }
            }
        }
    }
}

@Composable
internal fun GetLocationButton(
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
            viewModel.getLocation()
        } else {
            // Permission Denied: Do something
            Log.d("TAG", "PERMISSION DENIED")
        }
    }

    Button(onClick = {
        if (locationViewState.receivingUpdates) {
            viewModel.stopLocationUpdates()
        } else {
            getLocation(context, viewModel, launcher)
        }
    }) {
        when {
            locationViewState.loadingLocation -> {
                CircularProgressIndicator(color = Color.White)
            }
            locationViewState.receivingUpdates -> {
                Text(text = "Stop")
            }
            else -> {
                Text(text = "Get Location")
            }
        }
    }
}

private fun getLocation(
    context: Context,
    viewModel: LocationViewModel,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    when (context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
        true -> {
            // Some work that requires permission
            viewModel.getLocation()
        }
        else -> {
            // Asking for permission
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

@Composable
internal fun UpdateLocationButton(
    viewModel: LocationViewModel,
    locationViewState: LocationViewModel.LocationViewState
) {
    Button(enabled = locationViewState.latitude != null && locationViewState.longitude != null,
        onClick = { viewModel.postLocation() }) {
        when {
            locationViewState.postingLocation -> {
                CircularProgressIndicator(color = Color.White)
            }
            else -> {
                Text(text = "Post Location")
            }
        }
    }
}