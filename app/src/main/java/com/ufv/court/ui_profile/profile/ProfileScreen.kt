package com.ufv.court.ui_profile.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ufv.court.R
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.app.theme.Manatee
import com.ufv.court.app.theme.ShipCove
import com.ufv.court.core.core_common.util.toMaskedPhone
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

@Composable
fun ProfileScreen(
    openLogin: () -> Unit,
    openChangePassword: () -> Unit,
    openEditProfile: () -> Unit,
    openPhoto: (String) -> Unit
) {
    ProfileScreen(
        viewModel = hiltViewModel(),
        openLogin = openLogin,
        openChangePassword = openChangePassword,
        openEditProfile = openEditProfile,
        openPhoto = openPhoto
    )
}

@Composable
private fun ProfileScreen(
    viewModel: ProfileViewModel,
    openLogin: () -> Unit,
    openChangePassword: () -> Unit,
    openEditProfile: () -> Unit,
    openPhoto: (String) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ProfileViewState.Empty)

    ProfileScreen(
        state = viewState,
        openLogin = openLogin,
        openChangePassword = openChangePassword,
        openEditProfile = openEditProfile,
        openPhoto = openPhoto
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
fun ProfileScreen(
    state: ProfileViewState,
    openLogin: () -> Unit,
    openChangePassword: () -> Unit,
    openEditProfile: () -> Unit,
    openPhoto: (String) -> Unit,
    action: (ProfileAction) -> Unit
) {
    if (state.placeholder) {
        CircularLoading()
    } else {
        SwipeRefresh(
            modifier = Modifier
                .padding(bottom = 56.dp) // toolbar height
                .fillMaxSize(),
            state = rememberSwipeRefreshState(state.isRefreshing),
            onRefresh = { action(ProfileAction.Refresh) },
            indicator = { swipeState, trigger ->
                SwipeRefreshIndicator(
                    state = swipeState,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileTopBar(state = state, openPhoto = openPhoto)
                ProfileHorizontalDivisor()
                ChangePasswordItem(onClick = openChangePassword)
                ProfileHorizontalDivisor()
                ChangePersonalDataItem(onClick = openEditProfile)
                ProfileHorizontalDivisor()
                TermsItem {
                }
                ProfileHorizontalDivisor()
                VersionItem {
                }
                ProfileHorizontalDivisor()
                LogoutItem(onClick = { action(ProfileAction.ChangeShowConfirmLogoutDialog(true)) })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    ConfirmLogoutDialog(
        showDialog = state.showConfirmLogoutDialog,
        action = action,
        openLogin = openLogin
    )
    ErrorsDialog(error = state.error, action = action)
}

@Composable
private fun ProfileTopBar(state: ProfileViewState, openPhoto: (String) -> Unit) {
    Spacer(modifier = Modifier.height(32.dp))
    RoundedImage(
        modifier = Modifier
            .clip(CircleShape)
            .clickable { openPhoto(state.image) },
        image = state.image
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        modifier = Modifier.testTag("NameProfile"),
        text = state.name,
        style = MaterialTheme.typography.h5
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        modifier = Modifier.testTag("EmailProfile"),
        text = state.email,
        style = MaterialTheme.typography.body1,
        color = ShipCove
    )
    if (state.phone.isNotBlank()) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.testTag("PhoneProfile"),
            text = state.phone.toMaskedPhone(),
            style = MaterialTheme.typography.body1,
            color = ShipCove
        )
    }
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun ChangePasswordItem(onClick: () -> Unit) {
    ProfileItem(text = stringResource(R.string.change_password), onClick = onClick) {
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null, tint = Manatee)
    }
}

@Composable
private fun ChangePersonalDataItem(onClick: () -> Unit) {
    ProfileItem(text = stringResource(R.string.edit_personal_data), onClick = onClick) {
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null, tint = Manatee)
    }
}

@Composable
private fun TermsItem(onClick: () -> Unit) {
    ProfileItem(
        text = stringResource(R.string.terms_and_conditions),
        onClick = onClick
    )
}

@Composable
private fun VersionItem(onClick: () -> Unit) {
    ProfileItem(text = stringResource(R.string.version), onClick = onClick) {
        Text(
            text = "0.0.1", // todo app version
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal)
        )
    }
}

@Composable
private fun LogoutItem(onClick: () -> Unit) {
    ProfileItem(
        text = stringResource(R.string.logout),
        colorText = MaterialTheme.colors.primary,
        onClick = onClick
    )
}

@Composable
private fun ProfileHorizontalDivisor() {
    HorizontalDivisor(modifier = Modifier.padding(start = 16.dp))
}

@Composable
private fun ProfileItem(
    colorText: Color = BlackRock,
    text: String,
    onClick: () -> Unit = { },
    rightContent: @Composable RowScope.() -> Unit = { }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(64.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Normal,
                color = colorText
            )
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            rightContent()
        }
    }
}

@Composable
private fun ConfirmLogoutDialog(
    showDialog: Boolean,
    action: (ProfileAction) -> Unit,
    openLogin: () -> Unit
) {
    if (showDialog) {
        TwoButtonsDialog(
            title = stringResource(R.string.confirmation),
            message = stringResource(R.string.really_want_to_logout),
            leftButtonText = stringResource(R.string.cancel),
            rightButtonText = stringResource(R.string.confirm),
            onLeftButtonClick = { action(ProfileAction.ChangeShowConfirmLogoutDialog(false)) },
            onRightButtonClick = { action(ProfileAction.ConfirmLogout(openLogin)) },
            onDismiss = { action(ProfileAction.ChangeShowConfirmLogoutDialog(false)) }
        )
    }
}

@Composable
private fun ErrorsDialog(
    error: Throwable?,
    action: (ProfileAction) -> Unit
) {
    if (error == ProfileError.LogoutError) {
        OneButtonErrorDialog(message = stringResource(R.string.could_not_logout)) {
            action(ProfileAction.CleanErrors)
        }
    }
}
