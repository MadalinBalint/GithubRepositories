package com.mendelin.githubrepo.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Repository(
    val id: Int,
    val ownerAvatar: String,
    val ownerName: String,
    val repositoryName: String,
    val repositoryTitle: String,
    val repositoryDesc: String,
    val repositoryUrl: String,

    val language: String?,
    val licenseType: String?,
    val licenseUrl: String?,
    val topics: String?,

    val endOfListMessage: String? = null
): Parcelable
