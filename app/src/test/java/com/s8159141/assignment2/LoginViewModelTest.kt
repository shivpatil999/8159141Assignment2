package com.s8159141.assignment2

import com.s8159141.assignment2.ui.login.LoginUiState
import com.s8159141.assignment2.ui.login.LoginViewModel
import com.s8159141.assignment2.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepo = FakeAuthRepository()
    private val viewModel = LoginViewModel(fakeRepo)

    @Test
    fun `blank input shows error and does not call repository`() = runTest {
        viewModel.login("", "")
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals(null, fakeRepo.lastUsername)
    }

    @Test
    fun `successful login emits Success with keypass`() = runTest {
        fakeRepo.result = Result.Success("architecture")
        viewModel.login("8159141", "Shiv Bhagyesh")
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals("architecture", (state as LoginUiState.Success).keypass)
        assertEquals("8159141", fakeRepo.lastUsername)
    }

    @Test
    fun `failed login emits Error with message`() = runTest {
        fakeRepo.result = Result.Error("Incorrect username or password.")
        viewModel.login("8159141", "wrong")
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("Incorrect username or password.", (state as LoginUiState.Error).message)
    }

    @Test
    fun `resetState returns to idle`() = runTest {
        viewModel.login("", "")
        viewModel.resetState()
        assertEquals(LoginUiState.Idle, viewModel.uiState.value)
    }
}
