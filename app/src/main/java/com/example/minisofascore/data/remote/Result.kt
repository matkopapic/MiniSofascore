package com.example.minisofascore.data.remote

sealed class Result<out T> {

    data class Success<T>(val data: T) : Result<T>()

    class Error(val error: Throwable) : Result<Nothing>()
}