package com.ufv.court.ui_home.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = HomeViewState.Empty)

    HomeScreen(viewState) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
fun HomeScreen(
    state: HomeViewState,
    action: (HomeAction) -> Unit
) {
    Column {
        Text(text = "New Home")
    }
}