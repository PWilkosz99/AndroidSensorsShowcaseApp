package com.agh.sensorsapp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Column {
        TopAppBar(
            title = { Text(text = "Home Screen") },
        )

        Text(text = "This is the Home Screen content.")
    }
}
