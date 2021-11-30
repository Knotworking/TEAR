package com.knotworking.tear.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.knotworking.tear.ui.theme.TEARTheme
import org.koin.android.viewmodel.ext.android.viewModel

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class MainActivity : AppCompatActivity() {

    private val locationViewModel: LocationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TEARTheme {
                LocationContent(locationViewModel)
            }
        }
    }

    private fun requestForegroundPermissions() {
        Log.d("TAG", "Request foreground only permission")
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("TAG", "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d("TAG", "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    locationViewModel.startLocationUpdates()
                else -> {
                    // Permission denied.
                    Log.i("TAG", "Permission denied")
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

@Composable
internal fun LocationContent(viewModel: LocationViewModel) {
    val locationViewState by viewModel.locationViewState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${locationViewState.kmProgress?.toInt()}km")
        Text("${String.format("%.2f", locationViewState.percentageProgress)}%")
        Text("${String.format("%.2f", locationViewState.distanceToTrail?.div(1000))}km to trail")
        Text(text = "${locationViewState.latitude?.toString()?.plus(" lat, ") ?: ""}${"${locationViewState.longitude?.toString()} lon" ?: ""}")
        Spacer(modifier = Modifier.height(16.dp))
        LocationButton(viewModel = viewModel, locationViewState = locationViewState)
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
            // Some works that require permission
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