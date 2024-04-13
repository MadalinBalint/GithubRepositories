package com.mendelin.githubrepo.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mendelin.githubrepo.BuildConfig
import com.mendelin.githubrepo.data.remote.GithubApi
import com.mendelin.githubrepo.data.repository.GithubRepository
import com.mendelin.githubrepo.domain.usecase.SearchRepositoriesUseCase
import com.mendelin.githubrepo.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    private const val READ_TIMEOUT = 60L
    private const val CONNECTION_TIMEOUT = 60L
    private const val TRY_COUNT = 3
    private const val TRY_PAUSE_BETWEEN = 1000L

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.header("Accept", "application/vnd.github+json")
            builder.header("Authorization", "Bearer ${BuildConfig.API_KEY}")
            builder.header("X-GitHub-Api-Version", "2022-11-28")

            var response = chain.proceed(builder.build())

            /* Automatically retry the call for N times if you receive a server error (5xx) */
            var tryCount = 0
            while (response.code in 500..599 && tryCount < TRY_COUNT) {
                try {
                    response.close()
                    Thread.sleep(TRY_PAUSE_BETWEEN)
                    response = chain.proceed(builder.build())
                    Timber.e("Request is not successful - ${tryCount + 1}")
                } catch (e: Exception) {
                    Timber.e(e.localizedMessage)
                } finally {
                    tryCount++
                }
            }

            /* Intercept empty body response */
            if (response.body?.contentLength() == 0L) {
                val contentType: MediaType? = response.body!!.contentType()
                val body = "{}".toResponseBody(contentType)
                return@Interceptor response.newBuilder().body(body).build()
            }

            return@Interceptor response
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: Interceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val cacheSize = 8L * 1024L * 1024L
        val cache = Cache(context.cacheDir, cacheSize)

        return OkHttpClient.Builder().apply {
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(interceptor)
            cache(cache)

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            if (BuildConfig.DEBUG) {
                addInterceptor(logging)
            }
        }.build()
    }

    @Provides
    @Singleton
    fun provideGsonClient(): Gson =
        GsonBuilder().setLenient()
            .setDateFormat(Constants.DATE_FORMAT)
            .create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGithubApi(retrofit: Retrofit): GithubApi =
        retrofit.create(GithubApi::class.java)

    @Provides
    fun provideSearchRepositoriesUseCase(
        repository: GithubRepository,
        gson: Gson
    ): SearchRepositoriesUseCase =
        SearchRepositoriesUseCase(repository, gson)

    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}
