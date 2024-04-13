package com.mendelin.githubrepo.data.repository

import com.mendelin.githubrepo.data.model.RepositoriesResponse
import com.mendelin.githubrepo.data.remote.GithubApi
import retrofit2.Response
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(private val api: GithubApi) : GithubRepository {
   override suspend fun searchRepositories(
        search: String,
        perPage: Int,
        page: Int
    ): Response<RepositoriesResponse> =
        api.searchRepositories(
            search = search,
            perPage = perPage,
            page = page,
        )
}
