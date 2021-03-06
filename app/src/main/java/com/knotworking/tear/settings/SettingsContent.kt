package com.knotworking.tear.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalComposeUiApi
@Composable
fun SettingsContent(viewModel: SettingsViewModel, onClose: () -> Unit) {
    val settingsViewState by viewModel.settingsViewState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    Snackbar(
        hideSnackbar = { viewModel.hideSnackbar() },
        settingsViewState = settingsViewState,
        scaffoldState = scaffoldState
    )
    Scaffold(scaffoldState = scaffoldState) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Settings", style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = { viewModel.getNewToken() },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    when {
                        settingsViewState.loginInProgress -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        else -> {
                            Text(text = "Get Token", color = Color.White)
                        }
                    }

                }
                MarkerTextInput(viewModel = viewModel, settingsViewState = settingsViewState)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun MarkerTextInput(
    viewModel: SettingsViewModel,
    settingsViewState: SettingsViewModel.SettingsViewState
) {
    var text by remember { mutableStateOf(settingsViewState.markerText) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Marker text") }
        )
        Button(
            onClick = {
                viewModel.setMarkerText(text)
                keyboardController?.hide()
            },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = "Set", color = Color.White)
        }

    }
}

@Composable
internal fun Snackbar(
    hideSnackbar: () -> Unit,
    settingsViewState: SettingsViewModel.SettingsViewState,
    scaffoldState: ScaffoldState
) {
    LaunchedEffect(settingsViewState) {
        if (settingsViewState.snackbarText != null) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = settingsViewState.snackbarText,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> hideSnackbar()
                SnackbarResult.Dismissed -> hideSnackbar()
            }
        }
    }
}