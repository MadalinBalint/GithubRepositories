package com.mendelin.githubrepo.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mendelin.githubrepo.data.model.RepositoryModel
import com.mendelin.githubrepo.data.model.toRepository
import com.mendelin.githubrepo.domain.Resource
import com.mendelin.githubrepo.domain.model.Repository
import com.mendelin.githubrepo.domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.flow.lastOrNull
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

interface PageStatusCallback {
    fun onPageLoading()
    fun onPageSuccess(page: Int)
    fun onPageError(message: String)
}

class GithubRepoPageSource @Inject constructor(
    private val useCase: SearchRepositoriesUseCase,
    private val query: String,
    private val showEolAsError: Boolean,
    private val callback: PageStatusCallback
) : PagingSource<Int, Repository>() {
    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        return try {
            val currentPage = params.key ?: 1

            callback.onPageLoading()
            val response = useCase(query, ITEMS_PER_PAGE, currentPage).lastOrNull()
            Timber.d("Current page = $currentPage")

            if (response != null) {
                when (response) {
                    is Resource.Success -> {
                        if (response.data != null) {
                            val totalItems = response.data.total_count
                            var maxPages = totalItems / ITEMS_PER_PAGE
                            if (totalItems % ITEMS_PER_PAGE > 0) maxPages += 1

                            callback.onPageSuccess(currentPage)
                            LoadResult.Page(
                                data = response.data.items.map(RepositoryModel::toRepository),
                                prevKey = if (currentPage <= 1) null else currentPage.minus(1),
                                nextKey = if (currentPage < maxPages) currentPage.plus(1) else null
                            )
                        } else {
                            val message = response.message ?: "Received null data in response!"
                            callback.onPageError(message)
                            LoadResult.Error(Exception(message))
                        }
                    }

                    is Resource.Error -> {
                        val message = response.message ?: "Unknown error"
                        callback.onPageError(message)
                        LoadResult.Error(Exception(message))
                    }
                }
            } else {
                val message = "Received a null response!"
                callback.onPageError(message)
                LoadResult.Error(Exception(message))
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private fun getEndOfListRepository(msg: String) =
        Repository(
            id = -1,
            ownerAvatar = "",
            ownerName = "",
            repositoryName = "",
            repositoryTitle = "",
            repositoryDesc = "",
            repositoryUrl = "",

            language = "",
            licenseType = "",
            licenseUrl = "",
            topics = "",

            endOfListMessage = msg
        )

    companion object {
        const val ITEMS_PER_PAGE = 30
    }
}
