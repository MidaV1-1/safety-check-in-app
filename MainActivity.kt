package mida.secure.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import mida.secure.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CountdownTimer(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CountdownTimer(modifier: Modifier = Modifier) {
    var secondsRemaining by remember { mutableStateOf(24 * 60 * 60) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = isRunning) {
        while (isRunning && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining--
        }
    }

    val hours = secondsRemaining / 3600
    val minutes = (secondsRemaining % 3600) / 60
    val seconds = secondsRemaining % 60

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        
        Button(
            onClick = { isRunning = !isRunning },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(if (isRunning) "Pause" else "Resume")
        }
        
        Button(
            onClick = { 
                secondsRemaining = 24 * 60 * 60
                isRunning = true
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Reset Timer")
        }
    }
}