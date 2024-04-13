package com.mendelin.githubrepo.main

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.mendelin.githubrepo.domain.usecase.SearchRepositoriesUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class GithubRepoViewModelTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: GithubRepoViewModel
    private lateinit var useCase: SearchRepositoriesUseCase

    @Inject
    lateinit var gson: Gson

    @Before
    fun setUp() {
        hiltRule.inject()

        useCase = mockk<SearchRepositoriesUseCase>()
        viewModel = GithubRepoViewModel(useCase)
    }

    @Test
    fun githubRepoViewModel_uiState_isEmpty() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            assertThat(state).isInstanceOf(GithubRepoUiState::class.java)
            assertThat(state.errorMessage).isNull()
            assertThat(state.isLoading).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun githubRepoViewModel_repositoriesState_isPagingData() = runTest {
        viewModel.repositoriesState.test {
            val state = awaitItem()

            assertThat(state).isInstanceOf(PagingData::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }
}
