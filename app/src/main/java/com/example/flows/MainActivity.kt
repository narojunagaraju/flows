package com.example.flows

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flows.ui.theme.FlowsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/***
 * Channels are hot flows, means they emit data even if there are no consumers. So, it will cause loss of data.
 * Flows are mostly cold, they can't emit the data until there are consumers.
 */


class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowsTheme {
                LaunchedEffect(key1 = Unit) {

                    //We can emit values from onStart, onCompletion also.
                    GlobalScope.launch {
                        val data: Flow<Int> = producer()
                        data.onStart {
                            Log.e(TAG, "onStart: ")
                        }.onCompletion {
                            Log.e(TAG, "onCompletion: ")
                        }.onEach {
                            Log.e(TAG, "onEach: $it")
                        }.collect {
                            Log.e(TAG, "onCreate1: $it")
                        }
                    }

                    //Terminal operators ex: Collector/collect,first/last,toList()
                    GlobalScope.launch {
                        val data = producer()
                        Log.e(TAG, "onCreate: $data.first()")
                    }

                    //Non terminal operators: map,flatmap,filter
                    GlobalScope.launch {
                        val data = producer()
                        data.map { it * it }.collect {
                            Log.e(TAG, "onCreateMap: $it" )
                        }
                    }

                }
            }
        }
    }

    private fun producer() = flow<Int> { // suspend block, these flows internally creates a coroutine scope
        val list = listOf(1, 2, 3, 4, 5, 6)
        list.forEach {
            delay(1000)
            emit(it)
        }
    }
}


