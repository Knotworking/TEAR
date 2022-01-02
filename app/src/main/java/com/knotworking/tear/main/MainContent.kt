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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun LocationContentWrapper(openSettings: () -> Unit, viewModel: LocationViewModel) {
    val locationViewState by viewModel.locationViewState.collectAsState()
    LocationContent(
        locationViewState = locationViewState,
        openSettings = openSettings,
        hideSnackbar = { viewModel.hideSnackbar() },
        updateLocation = { viewModel.updateLocation() },
        stopLocationUpdates = { viewModel.stopLocationUpdates() },
        postLocation = { viewModel.postLocation() })
}

class LocationViewStateParameterProvider :
    PreviewParameterProvider<LocationViewModel.LocationViewState> {
    override val values = sequenceOf(
        LocationViewModel.LocationViewState(
            kmProgress = 2568.0,
            percentageProgress = 51.4,
            distanceToTrail = 34.4,
            latitude = 42.704819,
            longitude = 27.899409,
            updatedAt = Instant.now()
        )
    )
}


@Preview
@Composable
internal fun LocationContent(
    @PreviewParameter(LocationViewStateParameterProvider::class) locationViewState: LocationViewModel.LocationViewState,
    openSettings: () -> Unit = {},
    hideSnackbar: () -> Unit = {},
    updateLocation: () -> Unit = {},
    stopLocationUpdates: () -> Unit = {},
    postLocation: () -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()

    Snackbar(
        hideSnackbar = hideSnackbar,
        locationViewState = locationViewState,
        scaffoldState = scaffoldState
    )
    Scaffold(scaffoldState = scaffoldState) {
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = openSettings,
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
                Text(text = "Updated: ${locationViewState.updatedAt?.let { formatTimestamp(it) } ?: "unknown"}")
                Spacer(modifier = Modifier.height(16.dp))
                GetLocationButton(
                    locationViewState = locationViewState,
                    updateLocation = updateLocation,
                    stopLocationUpdates = stopLocationUpdates
                )
                Spacer(modifier = Modifier.height(16.dp))
                UpdateLocationButton(
                    locationViewState = locationViewState,
                    postLocation = postLocation
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: Instant): String {
    val pattern = "dd/MM/yyyy HH:mm O"
    val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())

    return formatter.format(timestamp)
}

@Composable
internal fun Snackbar(
    hideSnackbar: () -> Unit,
    locationViewState: LocationViewModel.LocationViewState,
    scaffoldState: ScaffoldState
) {
    LaunchedEffect(locationViewState) {
        if (locationViewState.snackbarText != null) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = locationViewState.snackbarText,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> hideSnackbar()
                SnackbarResult.Dismissed -> hideSnackbar()
            }
        }
    }
}

@Composable
internal fun GetLocationButton(
    locationViewState: LocationViewModel.LocationViewState,
    updateLocation: () -> Unit,
    stopLocationUpdates: () -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("TAG", "PERMISSION GRANTED")
            updateLocation()
        } else {
            // Permission Denied: Do something
            Log.d("TAG", "PERMISSION DENIED")
        }
    }

    Button(onClick = {
        if (locationViewState.receivingUpdates) {
            stopLocationUpdates()
        } else {
            updateLocationWithPermission(
                context = context,
                launcher = launcher,
                updateLocation = updateLocation
            )
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

private fun updateLocationWithPermission(
    context: Context,
    updateLocation: () -> Unit,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    when (context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
        true -> {
            // Some work that requires permission
            updateLocation()
        }
        else -> {
            // Asking for permission
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

@Composable
internal fun UpdateLocationButton(
    locationViewState: LocationViewModel.LocationViewState,
    postLocation: () -> Unit
) {
    Button(
        enabled = locationViewState.latitude != null && locationViewState.longitude != null,
        onClick = postLocation
    ) {
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