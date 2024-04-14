package com.mendelin.githubrepo.main

data class GithubRepoUiState(
    var isLoading: Boolean = false,
    var emptyListMessage: String? = null,
    var errorMessage: String? = null
)
