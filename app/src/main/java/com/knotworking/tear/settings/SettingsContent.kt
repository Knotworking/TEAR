package com.knotworking.tear.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsContent(viewModel: SettingsViewModel) {
    val settingsViewState by viewModel.settingsViewState.collectAsState()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Settings")
            Button(onClick = { viewModel.getNewToken() }) {
                when {
                    settingsViewState.loginInProgress -> {
                        CircularProgressIndicator(color = Color.White)
                    }
                    else -> {
                        Text(text = "Get Token")
                    }
                }

            }
            MarkerTextInput(viewModel = viewModel, settingsViewState = settingsViewState)
        }
    }
}

@Composable
fun MarkerTextInput(
    viewModel: SettingsViewModel,
    settingsViewState: SettingsViewModel.SettingsViewState
) {
    var text by remember { mutableStateOf(settingsViewState.markerText) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Marker text") }
        )
        Button(onClick = { viewModel.setMarkerText(text) }) {
            Text(text = "Set")
        }

    }
}