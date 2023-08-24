package com.example.core.hilt

import com.example.core.utils.DispatcherProvider
import com.example.core.utils.RuntimeDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDispatcher(): DispatcherProvider {
        return RuntimeDispatcherProvider()
    }
}