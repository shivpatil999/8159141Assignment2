package com.s8159141.assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s8159141.assignment2.data.model.Entity
import com.s8159141.assignment2.data.repository.DashboardRepository
import com.s8159141.assignment2.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** UI state for the Dashboard screen. */
sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(val entities: List<Entity>) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

/** Loads the entity list for the given keypass through [DashboardRepository]. */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    /** Called once with the keypass from the login response. */
    fun loadEntities(keypass: String) {
        _uiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = dashboardRepository.getEntities(keypass)) {
                is Result.Success -> DashboardUiState.Success(result.data)
                is Result.Error -> DashboardUiState.Error(result.message)
            }
        }
    }
}
