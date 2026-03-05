package com.swimscape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.swimscape.ui.navigation.SwimScapeNavGraph
import com.swimscape.ui.theme.SwimScapeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as SwimScapeApplication
        setContent {
            SwimScapeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SwimScapeNavGraph(repository = app.repository)
                }
            }
        }
    }
}
