package com.knotworking.tear

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val wordViewModel: WordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordViewModel.requestNewWord()
        setContent {
            RandomWordContent(viewModel = wordViewModel)
        }
    }
}