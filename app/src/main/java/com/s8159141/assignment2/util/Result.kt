package com.s8159141.assignment2.util

/**
 * A small result wrapper so the UI layer can react to success or failure without
 * handling exceptions directly. [Result.Error] carries a message ready to show the user.
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String) : Result<Nothing>
}
