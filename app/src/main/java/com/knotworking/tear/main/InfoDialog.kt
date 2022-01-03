package com.knotworking.tear.main

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Preview
@Composable
internal fun InfoDialog(
    @PreviewParameter(LocationViewStateParameterProvider::class) locationViewState: LocationViewModel.LocationViewState,
    onDismiss: () -> Unit = {}
) {
    val itemSpacing = 4.dp

    Dialog(onDismissRequest = onDismiss) {
        Box(
            Modifier
                .wrapContentHeight()
                .width(260.dp)
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(5.dp))
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "Info",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = itemSpacing)
                )
                Text(
                    "${
                        String.format(
                            "%.2f",
                            locationViewState.distanceToTrail?.div(1000)
                        )
                    }km from main route",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = itemSpacing)
                )
                Text(
                    text = "${
                        locationViewState.latitude?.toString()?.plus(" lat, ") ?: ""
                    }${"${locationViewState.longitude?.toString()} lon"}",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = itemSpacing)
                )
                Text(text = "Updated: ${locationViewState.updatedAt?.let { timeAgo(it) } ?: "unknown"}",
                    style = MaterialTheme.typography.body2)
                Text(text = locationViewState.updatedAt?.let { formatTimestamp(it) } ?: "unknown",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = itemSpacing))
            }

        }
    }
}

private fun formatTimestamp(timestamp: Instant): String {
    val pattern = "dd/MM/yyyy HH:mm O"
    val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())

    return formatter.format(timestamp)
}

private fun timeAgo(timestamp: Instant): String {
    return DateUtils.getRelativeTimeSpanString(timestamp.toEpochMilli()).toString()
}