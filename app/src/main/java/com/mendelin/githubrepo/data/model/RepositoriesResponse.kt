package com.mendelin.githubrepo.data.model

import androidx.annotation.Keep

@Keep
data class RepositoriesResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<RepositoryModel>,
)

@Keep
data class UnprocessableEntityResponse(
    val message: String,
    val documentation_url: String,
)

