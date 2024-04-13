package com.mendelin.githubrepo.data.remote

import com.mendelin.githubrepo.data.model.RepositoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") search: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Response<RepositoriesResponse>
}
