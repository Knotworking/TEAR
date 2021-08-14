package com.knotworking.domain.usecase

sealed class Error {
    object NetworkError : Error()
    object GenericError : Error()
    object ResponseError : Error()
    object PersistenceError : Error()
}