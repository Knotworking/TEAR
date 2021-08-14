package com.knotworking.tear

import androidx.lifecycle.ViewModel
import com.knotworking.domain.usecase.Result
import com.knotworking.domain.usecase.Error
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext
//@ObsoleteCoroutinesApi
abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job = Job()
    protected abstract val receiveChannel: ReceiveChannel<Result<Any, Error>>

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    abstract fun resolve(value: Result<Any, Error>)

    init {
        processStream()
    }

    private fun processStream() {
        launch {
            receiveChannel.consumeEach {
                resolve(it)
            }
        }
    }

    override fun onCleared() {
        receiveChannel.cancel()
        coroutineContext.cancel()
        super.onCleared()
    }
}