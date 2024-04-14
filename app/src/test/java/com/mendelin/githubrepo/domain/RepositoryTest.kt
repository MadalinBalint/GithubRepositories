package com.mendelin.githubrepo.domain

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mendelin.githubrepo.data.fake.FakeRepositoryModel
import com.mendelin.githubrepo.data.model.RepositoriesResponse
import com.mendelin.githubrepo.data.model.ErrorResponse
import com.mendelin.githubrepo.data.model.toRepository
import com.mendelin.githubrepo.data.remote.GithubApi
import com.mendelin.githubrepo.data.repository.GithubRepositoryImpl
import com.mendelin.githubrepo.util.Constants
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositoryTest {
    @MockK
    lateinit var githubApi: GithubApi

    private lateinit var objectUnderTest: GithubRepositoryImpl
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        objectUnderTest = GithubRepositoryImpl(githubApi)
        gson = GsonBuilder().setLenient()
            .setDateFormat(Constants.DATE_FORMAT)
            .create()
    }

    @Test
    fun repositoryModel_toRepository_has_correct_data() {
        /* Given */
        val id = 1
        val model = FakeRepositoryModel.getRepositoryModel(id)

        /* When */
        val result = model.toRepository()

        /* Then */
        assertThat(result.id).isEqualTo(id)
        assertThat(result.ownerAvatar).isEqualTo("https://avatars.githubusercontent.com/u/878437?v=4")
        assertThat(result.ownerName).isEqualTo("JetBrains$id")
        assertThat(result.repositoryName).isEqualTo("kotlin$id")
        assertThat(result.repositoryTitle).isEqualTo("JetBrains$id/kotlin$id")
        assertThat(result.repositoryDesc).isEqualTo("The Kotlin Programming Language.")
        assertThat(result.repositoryUrl).isEqualTo("https://github.com/JetBrains/kotlin")

        assertThat(result.language).isEqualTo("Kotlin")
        assertThat(result.licenseType).isEqualTo("MIT License")
        assertThat(result.licenseUrl).isEqualTo("https://api.github.com/licenses/mit")
        assertThat(result.topics).isEqualTo("compiler, gradle-plugin, intellij-plugin, kotlin, kotlin-library, maven-plugin, programming-language, wasm, webassembly")
    }

    @Test
    fun searchRepositories_called_and_returns_success_contains_valid_data() =
        runTest {
            /* Given */
            val expectedResponse = RepositoriesResponse(
                total_count = 1,
                incomplete_results = false,
                items = listOf(FakeRepositoryModel.getRepositoryModel(1))
            )
            coEvery { githubApi.searchRepositories(any(), any(), any()) } returns Response.success(
                expectedResponse
            )

            /* When */
            val actualResponse = objectUnderTest.searchRepositories("kotlin", 10, 1)

            /* Then */
            assertThat(actualResponse.isSuccessful).isTrue()
            assertThat(actualResponse.body()?.incomplete_results).isFalse()
            assertThat(actualResponse.body()?.total_count).isEqualTo(1)
            assertThat(actualResponse.body()?.items).isEqualTo(expectedResponse.items)
        }

    @Test
    fun searchRepositories_called_and_returns_422_contains_error() =
        runTest {
            /* Given */
            val expectedResponse = ErrorResponse(
                message = "Only the first 1000 search results are available",
                documentation_url = "https://docs.github.com/v3/search/",
            )
            coEvery {
                githubApi.searchRepositories(any(), any(), any())
            } returns Response.error(
                422,
                "{\"message\":\"Only the first 1000 search results are available\",\"documentation_url\":\"https://docs.github.com/v3/search/\"}".toResponseBody()
            )

            /* When */
            val actualResponse = objectUnderTest.searchRepositories("kotlin", 10, 60)

            /* Then */
            assertThat(actualResponse.isSuccessful).isFalse()
            assertThat(actualResponse.code()).isEqualTo(422)

            val errorBody = actualResponse.errorBody()?.string()
            assertThat(errorBody).isNotEmpty()

            val body = gson.fromJson(errorBody, ErrorResponse::class.java)
            assertThat(body).isNotNull()
            assertThat(body).isEqualTo(expectedResponse)
        }
}
