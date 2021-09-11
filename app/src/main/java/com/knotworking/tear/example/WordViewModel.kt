package com.knotworking.tear.example

import com.knotworking.domain.example.GetRandomWordUseCase
import com.knotworking.tear.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*

internal class WordViewModel(
    private val getRandomWordUseCase: GetRandomWordUseCase
) : BaseViewModel() {
    val wordViewState: StateFlow<WordViewState>
        get() = _wordViewState
    private var _wordViewState = MutableStateFlow<WordViewState>(
        WordViewState()
    )

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        // handle error
        _wordViewState.value = _wordViewState.value.copy(hasError = true)
    }

    fun requestNewWord() {
        launchInViewModelScope {
            getRandomWordUseCase(Unit).onStart {
                _wordViewState.value = WordViewState(loading = true)
            }.catch {
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