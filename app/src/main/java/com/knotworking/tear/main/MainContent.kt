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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knotworking.tear.ui.theme.SecondaryGrey
import com.knotworking.tear.ui.theme.YellowSecondary
import java.time.Instant

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
    val showInfoDialog = remember { mutableStateOf(false) }

    Snackbar(
        hideSnackbar = hideSnackbar,
        locationViewState = locationViewState,
        scaffoldState = scaffoldState
    )
    Scaffold(scaffoldState = scaffoldState) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (locationViewState.updatedAt != null) {
                IconButton(
                    onClick = { showInfoDialog.value = true },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(imageVector = Icons.Filled.Info, contentDescription = "Info")
                }
            }

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
                TrailProgressIndicator(
                    locationViewState = locationViewState,
                    progressSize = 200f
                )
                Spacer(modifier = Modifier.height(16.dp))
                GetLocationButton(
                    locationViewState = locationViewState,
                    updateLocation = updateLocation,
                    stopLocationUpdates = stopLocationUpdates
                )
                Spacer(modifier = Modifier.height(16.dp))
                PostLocationButton(
                    locationViewState = locationViewState,
                    postLocation = postLocation
                )
            }

            if (showInfoDialog.value) {
                InfoDialog(
                    locationViewState = locationViewState,
                    onDismiss = { showInfoDialog.value = false })
            }
        }
    }
}

@Composable
internal fun TrailProgressIndicator(
    locationViewState: LocationViewModel.LocationViewState,
    progressSize: Float
) {
    Box(
        Modifier.width(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = 1f,
            strokeWidth = 10.dp,
            color = SecondaryGrey,
            modifier = Modifier
                .height(progressSize.dp)
                .width(progressSize.dp)
        )
        CircularProgressIndicator(
            progress = locationViewState.percentageProgress?.div(100)?.toFloat() ?: 0f,
            strokeWidth = 10.dp,
            modifier = Modifier
                .height(progressSize.dp)
                .width(progressSize.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${String.format("%.1f", locationViewState.kmProgress)}km",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                "${String.format("%.2f", locationViewState.percentageProgress)}%",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
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
                Text(text = "Get Location", color = Color.White)
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
internal fun PostLocationButton(
    locationViewState: LocationViewModel.LocationViewState,
    postLocation: () -> Unit
) {
    Button(
        enabled = locationViewState.latitude != null && locationViewState.longitude != null,
        onClick = postLocation,
        colors = ButtonDefaults.buttonColors(backgroundColor = YellowSecondary)
    ) {
        when {
            locationViewState.postingLocation -> {
                CircularProgressIndicator(color = Color.White)
            }
            else -> {
                Text(text = "Post Location", color = Color.White)
            }
        }
    }
}