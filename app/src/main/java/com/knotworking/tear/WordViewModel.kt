package com.knotworking.tear

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knotworking.domain.usecase.Error
import com.knotworking.domain.usecase.GetRandomWordUseCase
import com.knotworking.domain.usecase.None
import com.knotworking.domain.usecase.Result
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel

@ObsoleteCoroutinesApi
class WordViewModel(private val getRandomWordUseCase: GetRandomWordUseCase) : BaseViewModel() {
    override val receiveChannel: ReceiveChannel<Result<Any, Error>>
        get() = getRandomWordUseCase.receiveChannel

    private val _word = MutableLiveData<String>().apply { value = "" }
    val word: LiveData<String> = _word

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    override fun resolve(value: Result<Any, Error>) {
        value.handleResult(::handleSuccess, ::handleFailure, ::handleState)
    }

    fun requestNewWord() {
        getRandomWordUseCase(None())
    }

    fun handleSuccess(data: Any) {
        val word = data as String
        _word.postValue(word)
    }

    fun handleFailure(error: Error) {

    }

    fun handleState(state: Result.State) {
        _dataLoading.postValue(state is Result.State.Loading)
    }
}