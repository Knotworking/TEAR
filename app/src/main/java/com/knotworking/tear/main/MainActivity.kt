package com.knotworking.tear.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.knotworking.tear.nav.NavigationComponent
import com.knotworking.tear.ui.theme.TEARTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TEARTheme {
                NavigationComponent()
            }
        }
    }
}