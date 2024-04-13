package com.mendelin.githubrepo.di


import com.mendelin.githubrepo.data.fake.FakeGithubRepositoryImpl
import com.mendelin.githubrepo.data.repository.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
@Module
class FakeRepositoryModule {

    @Provides
    @Singleton
    fun provideGithubRepository(): GithubRepository = FakeGithubRepositoryImpl()

}
