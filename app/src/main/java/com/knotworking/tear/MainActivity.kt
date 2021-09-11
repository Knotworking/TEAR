package com.knotworking.tear

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
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
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.knotworking.tear.ui.theme.TEARTheme
import org.koin.android.viewmodel.ext.android.viewModel

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class MainActivity : AppCompatActivity() {

    private val locationViewModel: LocationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestForegroundPermissions()
        }

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
        Text(text = locationViewState.latitude?.toString() ?: "")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            Log.i("TAG", "start button")
            viewModel.startLocationUpdates()
        }) {
            if (locationViewState.loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(text = "start")
            }
        }
    }
}