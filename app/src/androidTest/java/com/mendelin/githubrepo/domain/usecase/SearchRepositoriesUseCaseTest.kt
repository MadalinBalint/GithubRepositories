package com.mendelin.githubrepo.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.mendelin.githubrepo.data.fake.FakeGithubRepositoryImpl
import com.mendelin.githubrepo.data.repository.GithubRepository
import com.mendelin.githubrepo.domain.Resource
import com.mendelin.githubrepo.main.GithubRepoPageSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SearchRepositoriesUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var gson: Gson

    private lateinit var repository: GithubRepository
    private lateinit var useCase: SearchRepositoriesUseCase

    @Before
    fun setUp() {
        hiltRule.inject()

        repository = FakeGithubRepositoryImpl()
        useCase = SearchRepositoriesUseCase(repository, gson)
    }

    @Test
    fun searchRepositoriesUseCase_call_successful() = runTest {
        useCase("kotlin", GithubRepoPageSource.ITEMS_PER_PAGE, 1).test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data).isNotNull()
            assertThat(result.data?.total_count).isEqualTo(GithubRepoPageSource.ITEMS_PER_PAGE*2)
            assertThat(result.data?.items).isNotEmpty()
            assertThat(result.data?.items?.size).isEqualTo(GithubRepoPageSource.ITEMS_PER_PAGE)

            val first = result.data?.items?.firstOrNull()
            assertThat(first).isNotNull()
            assertThat(first?.id).isEqualTo(0)
            assertThat(first?.name).isEqualTo("kotlin0")

            cancelAndConsumeRemainingEvents()
        }
    }
}
