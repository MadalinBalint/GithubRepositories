package com.mendelin.githubrepo.data.fake

import com.mendelin.githubrepo.data.fake.FakeRepositoryModel.getRepositoryModel
import com.mendelin.githubrepo.data.model.RepositoriesResponse
import com.mendelin.githubrepo.data.repository.GithubRepository
import com.mendelin.githubrepo.main.GithubRepoPageSource
import retrofit2.Response

class FakeGithubRepositoryImpl : GithubRepository {
    override suspend fun searchRepositories(
        search: String,
        perPage: Int,
        page: Int
    ): Response<RepositoriesResponse> {
        return Response.success(
            RepositoriesResponse(
                total_count = GithubRepoPageSource.ITEMS_PER_PAGE * 2,
                incomplete_results = false,
                items = List(GithubRepoPageSource.ITEMS_PER_PAGE) { getRepositoryModel(it) },
            )
        )
    }
}
