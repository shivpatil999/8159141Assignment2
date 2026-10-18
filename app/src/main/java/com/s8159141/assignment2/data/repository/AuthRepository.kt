package com.s8159141.assignment2.data.repository

import com.s8159141.assignment2.data.model.LoginRequest
import com.s8159141.assignment2.data.remote.ApiService
import com.s8159141.assignment2.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

/** Repository interface so the ViewModel can be unit-tested with a fake. */
interface AuthRepository {
    /** Logs in and returns the keypass (topic name) on success. */
    suspend fun login(username: String, password: String): Result<String>
}

/**
 * Default implementation that calls the auth endpoint and maps HTTP results to
 * friendly messages. Network work runs on the injected IO dispatcher.
 */
class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    @Named("campus") private val campus: String,
    @Named("io") private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<String> =
        withContext(ioDispatcher) {
            try {
                val response = api.login(campus, LoginRequest(username.trim(), password))
                val body = response.body()
                when {
                    response.isSuccessful && body != null -> Result.Success(body.keypass)
                    response.code() == 400 -> Result.Error("Incorrect username or password.")
                    response.code() == 404 -> Result.Error("User not found. Check your student ID.")
                    else -> Result.Error("Login failed (error ${response.code()}). Please try again.")
                }
            } catch (e: IOException) {
                Result.Error("No internet connection. Please check your network and try again.")
            } catch (e: Exception) {
                Result.Error("Something went wrong. Please try again.")
            }
        }
}
