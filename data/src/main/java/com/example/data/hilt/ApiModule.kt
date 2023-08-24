package com.example.data.hilt

import com.example.data.api.*
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object ApiModule {

    @Singleton
    @Provides
    fun provideLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }

    @Singleton
    @Provides
    fun provideLoremPicsumServerConfig(): LoremPicsumServerConfig {
        return LoremPicsumServer.prod
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory
            .create(gson)
    }

    @Singleton
    @Provides
    fun provideLoremPicsumApi(
        logger: HttpLoggingInterceptor,
        loremPicsumServerConfig: LoremPicsumServerConfig,
        converterFactory: GsonConverterFactory
    ): LoremPicsumApi {
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(loremPicsumServerConfig.baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
            .create(LoremPicsumApi::class.java)
    }
}