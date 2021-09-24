package com.knotworking.tear.example

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.knotworking.tear.R

@Composable
internal fun RandomWordContent(viewModel: WordViewModel) {
    val wordViewState by viewModel.wordViewState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = wordViewState.word ?: "")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.requestNewWord() }) {
            if (wordViewState.loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(text = stringResource(R.string.newWord))
            }
        }
    }
}