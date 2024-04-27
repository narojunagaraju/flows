package com.example.flows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flows.ui.theme.FlowsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/***
 * Channels are hot flows, means they emit data even if there are no consumers. So, it will cause loss of data.
 * Flows are mostly cold, they can't emit the data until there are consumers.
 */
val channel = Channel<Int>()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowsTheme {
                // A surface container using the 'background' color from the theme
                producer()
                consumer()
            }
        }
    }
}

fun producer() {
    CoroutineScope(Dispatchers.Main).launch {
        channel.send(1)
        channel.send(2)
    }
}

fun consumer() {
    CoroutineScope(Dispatchers.Main).launch {
        channel.receive()
        channel.receive()
    }
}

