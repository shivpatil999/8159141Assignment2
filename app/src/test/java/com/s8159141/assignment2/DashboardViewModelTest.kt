package com.s8159141.assignment2

import com.s8159141.assignment2.ui.dashboard.DashboardUiState
import com.s8159141.assignment2.ui.dashboard.DashboardViewModel
import com.s8159141.assignment2.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepo = FakeDashboardRepository()
    private val viewModel = DashboardViewModel(fakeRepo)

    @Test
    fun `loadEntities success emits Success with list and passes keypass`() = runTest {
        val entities = listOf(sampleEntity("Eiffel Tower"), sampleEntity("Taj Mahal"))
        fakeRepo.result = Result.Success(entities)
        viewModel.loadEntities("architecture")
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        assertEquals(2, (state as DashboardUiState.Success).entities.size)
        assertEquals("architecture", fakeRepo.lastKeypass)
    }

    @Test
    fun `loadEntities failure emits Error`() = runTest {
        fakeRepo.result = Result.Error("No data found for this topic.")
        viewModel.loadEntities("bad")
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Error)
        assertEquals("No data found for this topic.", (state as DashboardUiState.Error).message)
    }
}
