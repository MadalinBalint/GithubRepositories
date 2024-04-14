package com.mendelin.githubrepo.domain.usecase

import com.google.gson.Gson
import com.mendelin.githubrepo.data.model.ErrorResponse
import com.mendelin.githubrepo.data.repository.GithubRepository
import com.mendelin.githubrepo.domain.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SearchRepositoriesUseCase @Inject constructor(
    private val repository: GithubRepository,
    private val gson: Gson,
) {
    operator fun invoke(search: String, pageItems: Int, page: Int) = flow {
        try {
            val apiResponse = repository.searchRepositories(search, pageItems, page)
            when {
                apiResponse.isSuccessful -> {
                    val body = apiResponse.body()
                    if (body != null) {
                        emit(Resource.Success(body))
                    } else {
                        emit(Resource.Error(message = "Null body exception"))
                    }
                }

                apiResponse.code() in 400..499 -> {
                    val json = apiResponse.errorBody()?.string()
                    val body = gson.fromJson(json, ErrorResponse::class.java)
                    if (body != null) {
                        emit(Resource.Error(message = "${body.message}\n${body.documentation_url}"))
                    } else {
                        emit(Resource.Error(message = "Null body exception on ${apiResponse.code()}"))
                    }
                }

                else -> {
                    emit(Resource.Error(message = apiResponse.message()))
                }
            }
        } catch (ex: Exception) {
            emit(Resource.Error(message = ex.localizedMessage ?: "Exception"))
        }
    }
}
