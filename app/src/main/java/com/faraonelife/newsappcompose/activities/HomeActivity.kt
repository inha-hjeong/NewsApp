package com.faraonelife.newsappcompose.activities

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.faraonelife.newsappcompose.states.HomeState
import com.faraonelife.newsappcompose.ui.screen.HomeScreen
import com.faraonelife.newsappcompose.viewmodels.HomeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeVM, HomeState>() {
    override val viewModel: HomeVM by viewModels()

    @Composable
    override fun Content() {
        HomeScreen(viewModel)
    }
}