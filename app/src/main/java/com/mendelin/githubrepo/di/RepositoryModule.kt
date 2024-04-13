package com.mendelin.githubrepo.di

import com.mendelin.githubrepo.data.remote.GithubApi
import com.mendelin.githubrepo.data.repository.GithubRepository
import com.mendelin.githubrepo.data.repository.GithubRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGithubRepository(api: GithubApi): GithubRepository =
        GithubRepositoryImpl(api)
}
