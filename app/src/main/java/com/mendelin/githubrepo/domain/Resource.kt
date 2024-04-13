package com.mendelin.githubrepo.domain

import androidx.annotation.Keep

@Keep
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message)
    class EndOfList<T>(data: T? = null, message: String) : Resource<T>(data, message)
}
