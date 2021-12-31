package com.ufv.court.ui_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.usecase.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ProfileAction>()

    private val _state: MutableStateFlow<ProfileViewState> =
        MutableStateFlow(ProfileViewState.Empty)
    val state: StateFlow<ProfileViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ProfileAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    is ProfileAction.ChangeShowConfirmLogoutDialog -> {
                        _state.value = state.value.copy(showConfirmLogoutDialog = action.show)
                    }
                    is ProfileAction.ConfirmLogout -> logout(action.onSuccess)
                }
            }
        }
    }

    private fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = logoutUserUseCase(Unit)
            _state.value = state.value.copy(showConfirmLogoutDialog = false)
            if (result is Result.Success) {
                onSuccess()
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = ProfileError.LogoutError)
            }
        }
    }

    fun submitAction(action: ProfileAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}