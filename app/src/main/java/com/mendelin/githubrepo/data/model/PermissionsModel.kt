package com.mendelin.githubrepo.data.model

import androidx.annotation.Keep

@Keep
data class PermissionsModel(
    val admin: Boolean,
    val maintain: Boolean,
    val push: Boolean,
    val triage: Boolean,
    val pull: Boolean,
)
