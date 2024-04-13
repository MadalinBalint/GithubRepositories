package com.mendelin.githubrepo.data.model

import androidx.annotation.Keep

@Keep
data class LicenseModel(
    val key: String,
    val name: String,
    val spdx_id: String,
    val url: String?,
    val node_id: String,
)
