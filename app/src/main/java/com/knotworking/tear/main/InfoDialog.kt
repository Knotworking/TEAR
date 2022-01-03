package com.knotworking.tear.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun InfoDialog(
    locationViewState: LocationViewModel.LocationViewState,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            Modifier
                .wrapContentHeight()
                .width(250.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(5.dp))
        ) {
            Column() {
                Text(
                    text = "My dialog",
                    //style = TextStyle(color = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.surface))
                )
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
            }

        }
    }
}

private fun formatTimestamp(timestamp: Instant): String {
    val pattern = "dd/MM/yyyy HH:mm O"
    val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())

    return formatter.format(timestamp)
}