package com.mendelin.githubrepo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mendelin.githubrepo.domain.model.Repository
import com.mendelin.githubrepo.domain.usecase.SearchRepositoriesUseCase
import com.mendelin.githubrepo.main.GithubRepoPageSource.Companion.ITEMS_PER_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubRepoViewModel @Inject constructor(
    private val useCase: SearchRepositoriesUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GithubRepoUiState())
    val uiState = _uiState.asStateFlow()

    private val _repositoriesState =
        MutableStateFlow<PagingData<Repository>>(PagingData.empty())
    val repositoriesState = _repositoriesState.asStateFlow()

    private fun pagingData(query: String): Flow<PagingData<Repository>> =
        Pager(PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = true)) {
            GithubRepoPageSource(
                useCase = useCase,
                query = query,
                callback = object : PageStatusCallback {
                    override fun onPageLoading() {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                emptyListMessage = null,
                                errorMessage = null,
                            )
                        }
                    }

                    override fun onPageSuccess(page: Int, items: Int) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                emptyListMessage = if (items == 0) "No repositories for '$query' were found. Please use another search query." else null,
                                errorMessage = null,
                            )
                        }
                    }

                    override fun onPageError(message: String) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                emptyListMessage = null,
                                errorMessage = message,
                            )
                        }
                    }
                })
        }.flow.cachedIn(viewModelScope)

    fun fetchRepositories(query: String) {
        viewModelScope.launch {
            pagingData(query).flowOn(ioDispatcher)
                .collectLatest { data ->
                    data.let { }
                    _repositoriesState.value = data
                }
        }
    }

    fun errorShown() {
        _uiState.update {
            it.copy(
                errorMessage = null,
            )
        }
    }
}
