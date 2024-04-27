package com.example.flows

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.example.flows.ui.theme.FlowsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/***
 * Channels are hot flows, means they emit data even if there are no consumers. So, it will cause loss of data.
 * Flows are mostly cold, they can't emit the data until there are consumers.
 */


class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowsTheme {
                LaunchedEffect(key1 = Unit) {
                    /**
                     * Flows preserves the coroutine context,if you want to change it you need to use flow on
                     */
                    GlobalScope.launch {
                        producer()
                            .flowOn(Dispatchers.IO)
                            .collect {
                                Log.e(TAG, "onCreate: $it")
                            }
                    }
                }
            }
        }
    }

    private fun producer(): Flow<Int> {
        //Shared flow is a kind of hot flow, it will emit the items even if there are no subscribers.
        // The new subscribers will receive the data from the point of subscription.
        // We can use replay if we want to give old data to new subscribers.
        val mutableSharedFlow = MutableSharedFlow<Int>()
        GlobalScope.launch {
            val list = listOf(1, 2, 3, 4, 5, 6)
            list.forEach {
                mutableSharedFlow.emit(it)
            }
        }
        return mutableSharedFlow
    }
}


