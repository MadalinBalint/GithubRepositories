package com.mendelin.githubrepo.data.repository

import com.mendelin.githubrepo.data.model.RepositoriesResponse
import retrofit2.Response

interface GithubRepository {
    suspend fun searchRepositories(
        search: String,
        perPage: Int,
        page: Int
    ): Response<RepositoriesResponse>
}
