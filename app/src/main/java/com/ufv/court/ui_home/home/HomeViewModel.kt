package com.ufv.court.ui_home.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.schedule_service.domain.usecases.GetSchedulesFreeSpaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSchedulesFreeSpaceUseCase: GetSchedulesFreeSpaceUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<HomeAction>()

    private val _state: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState.Empty)
    val state: StateFlow<HomeViewState> = _state

    init {
        getSchedulesFreeSpace()
        handleActions()
    }

    private fun getSchedulesFreeSpace() {
        viewModelScope.launch {
            val result = getSchedulesFreeSpaceUseCase(Unit)
            if (result is Result.Success) {
                _state.value = state.value.copy(schedules = result.data.filter {
                    it.timeInMillis > System.currentTimeMillis() && !it.cancelled
                })
            }
        }
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    HomeAction.CleanErrors -> _state.value = state.value.copy(error = null)
                }
            }
        }
    }

    fun submitAction(action: HomeAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
