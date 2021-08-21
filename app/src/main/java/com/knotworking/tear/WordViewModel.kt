package com.knotworking.tear

import com.knotworking.domain.usecase.GetRandomWordUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*

internal class WordViewModel(
    private val getRandomWordUseCase: GetRandomWordUseCase
) : BaseViewModel() {

    // TODO: how do the other projects do this
//    val loading = MutableStateFlow(false)
    val hasError = MutableStateFlow(false)

    val wordViewState: StateFlow<WordViewState>
        get() = _wordViewState
    private var _wordViewState = MutableStateFlow<WordViewState>(
        WordViewState()
    )

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        // handle error
//        _paywallViewState.value = _paywallViewState.value.copy(error = Error(message = R.string.__error)) // TODO: map error message
        hasError.value = true
    }

    fun requestNewWord() {
        launchInViewModelScope {
            getRandomWordUseCase(Unit).onStart {
                hasError.value = false
                _wordViewState.value = WordViewState(loading = true)
            }.catch {
                hasError.value = true
                _wordViewState.value = WordViewState(hasError = true)
            }.onCompletion {
            }.collect {
                _wordViewState.value = WordViewState(word = it)
            }
        }
    }

    data class WordViewState(
        val hasError: Boolean = false,
        val loading: Boolean = false,
        val word: String? = null
    )
}