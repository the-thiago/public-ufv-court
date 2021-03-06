package com.ufv.court.ui_myschedule.myschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduledAsParticipantUseCase
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduledByUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyScheduleViewModel @Inject constructor(
    private val getScheduledByUserUseCase: GetScheduledByUserUseCase,
    private val getScheduledAsParticipantUseCase: GetScheduledAsParticipantUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<MyScheduleAction>()

    private val _state: MutableStateFlow<MyScheduleViewState> =
        MutableStateFlow(MyScheduleViewState.Empty)
    val state: StateFlow<MyScheduleViewState> = _state

    init {
        getSchedulesForFirstTime()
        handleActions()
    }

    private fun getSchedulesForFirstTime() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            getScheduledAsParticipant()
            getScheduled()
            _state.value = state.value.copy(isLoading = false)
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = state.value.copy(isRefreshing = true)
            getScheduledAsParticipant()
            getScheduled()
            _state.value = state.value.copy(isRefreshing = false)
        }
    }

    private fun getScheduledAsParticipant() {
        viewModelScope.launch {
            val result = getScheduledAsParticipantUseCase(Unit)
            if (result is Result.Success) {
                _state.value = state.value.copy(scheduledAsParticipant = result.data)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private fun getScheduled() {
        viewModelScope.launch {
            val result = getScheduledByUserUseCase(Unit)
            if (result is Result.Success) {
                _state.value = state.value.copy(scheduled = result.data)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    MyScheduleAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    MyScheduleAction.Refresh -> refresh()
                }
            }
        }
    }

    fun submitAction(action: MyScheduleAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
