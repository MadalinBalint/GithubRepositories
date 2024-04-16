package com.mendelin.githubrepo.main

import androidx.paging.PagingSource
import com.google.gson.Gson
import com.mendelin.githubrepo.data.fake.FakeRepositoryModel
import com.mendelin.githubrepo.data.model.RepositoriesResponse
import com.mendelin.githubrepo.data.model.toRepository
import com.mendelin.githubrepo.data.remote.GithubApi
import com.mendelin.githubrepo.data.repository.GithubRepository
import com.mendelin.githubrepo.data.repository.GithubRepositoryImpl
import com.mendelin.githubrepo.domain.model.Repository
import com.mendelin.githubrepo.domain.usecase.SearchRepositoriesUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject

@HiltAndroidTest
class GithubRepoPageSourceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var api: GithubApi

    @Inject
    lateinit var gson: Gson

    private lateinit var useCase: SearchRepositoriesUseCase
    private lateinit var repository: GithubRepository
    private lateinit var usersPagingSource: GithubRepoPageSource

    @Before
    fun setUp() {
        hiltRule.inject()

        repository = GithubRepositoryImpl(api)
        useCase = SearchRepositoriesUseCase(repository, gson)
        usersPagingSource = GithubRepoPageSource(
            useCase = useCase,
            query = "kotlin",
            callback = object : PageStatusCallback {
                override fun onPageLoading() {}
                override fun onPageSuccess(page: Int, items: Int) {}
                override fun onPageError(message: String) {}
            }
        )
    }

    @Test
    fun githubRepoPageSource_returns_success_load_result() = runTest {
        /* Given */
        coEvery { api.searchRepositories(any(), any(), any()) } returns getResponseSuccess()

        val params = PagingSource
            .LoadParams
            .Refresh(
                key = 0,
                loadSize = GithubRepoPageSource.ITEMS_PER_PAGE,
                placeholdersEnabled = false
            )

        val expected = PagingSource
            .LoadResult
            .Page(
                data = getRepositoryList(),
                prevKey = null,
                nextKey = 1
            )

        /* When */
        val actual = usersPagingSource.load(params = params)

        /* Then */
        assertEquals(expected, actual)
    }

    @Test
    fun githubRepoPageSource_returns_error_load_result() = runTest {
        /* Given */
        coEvery { api.searchRepositories(any(), any(), any()) } returns getResponseError(400)

        val params = PagingSource
            .LoadParams
            .Refresh(
                key = 0,
                loadSize = GithubRepoPageSource.ITEMS_PER_PAGE,
                placeholdersEnabled = false
            )

        val expected = PagingSource
            .LoadResult
            .Error<Int, Repository>(Exception("Error"))::class.java

        /* When */
        val actual = usersPagingSource.load(params = params)::class.java

        /* Then */
        assertEquals(expected, actual)
    }

    @Test
    fun githubRepoPageSource_next_page_returns_success_load_result() = runTest {
        /* Given */
        coEvery { api.searchRepositories(any(), any(), any()) } returns getResponseSuccess()

        val params = PagingSource
            .LoadParams
            .Refresh(
                key = 1,
                loadSize = GithubRepoPageSource.ITEMS_PER_PAGE,
                placeholdersEnabled = false
            )

        val expected = PagingSource
            .LoadResult
            .Page(
                data = getRepositoryList(),
                prevKey = null,
                nextKey = 2
            )

        /* When */
        val actual = usersPagingSource.load(params = params)

        /* Then */
        assertEquals(expected, actual)
    }

    private fun getResponseSuccess() =
        Response.success(
            RepositoriesResponse(
                total_count = GithubRepoPageSource.ITEMS_PER_PAGE * 2,
                incomplete_results = false,
                items = List(GithubRepoPageSource.ITEMS_PER_PAGE) {
                    FakeRepositoryModel.getRepositoryModel(it)
                },
            )
        )

    private fun getResponseError(code: Int) =
        Response.error<RepositoriesResponse>(code, "{}".toResponseBody())

    private fun getRepositoryList() =
        List(GithubRepoPageSource.ITEMS_PER_PAGE) {
            FakeRepositoryModel.getRepositoryModel(it).toRepository()
        }
}
