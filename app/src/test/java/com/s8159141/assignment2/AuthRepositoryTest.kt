package com.s8159141.assignment2

import com.s8159141.assignment2.data.model.LoginResponse
import com.s8159141.assignment2.data.remote.ApiService
import com.s8159141.assignment2.data.model.DashboardResponse
import com.s8159141.assignment2.data.model.LoginRequest
import com.s8159141.assignment2.data.repository.AuthRepositoryImpl
import com.s8159141.assignment2.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

/** Tiny fake ApiService that returns a preset HTTP response for the auth call. */
private class FakeApi(
    private val code: Int,
    private val keypass: String? = null
) : ApiService {
    override suspend fun login(campus: String, body: LoginRequest): Response<LoginResponse> =
        if (keypass != null) Response.success(LoginResponse(keypass))
        else Response.error(code, "".toResponseBody("application/json".toMediaType()))

    override suspend fun getDashboard(keypass: String): Response<DashboardResponse> =
        Response.success(DashboardResponse(emptyList(), 0))
}

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `successful response returns keypass`() = runTest(dispatcher) {
        val repo = AuthRepositoryImpl(FakeApi(200, "architecture"), "footscray", dispatcher)
        val result = repo.login("8159141", "Shiv Bhagyesh")
        assertTrue(result is Result.Success)
        assertEquals("architecture", (result as Result.Success).data)
    }

    @Test
    fun `400 maps to incorrect credentials message`() = runTest(dispatcher) {
        val repo = AuthRepositoryImpl(FakeApi(400), "footscray", dispatcher)
        val result = repo.login("8159141", "wrong")
        assertTrue(result is Result.Error)
        assertEquals("Incorrect username or password.", (result as Result.Error).message)
    }

    @Test
    fun `404 maps to user not found message`() = runTest(dispatcher) {
        val repo = AuthRepositoryImpl(FakeApi(404), "footscray", dispatcher)
        val result = repo.login("0000", "x")
        assertTrue(result is Result.Error)
        assertEquals("User not found. Check your student ID.", (result as Result.Error).message)
    }
}
