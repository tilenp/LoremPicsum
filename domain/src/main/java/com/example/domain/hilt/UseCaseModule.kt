package com.example.domain.hilt

import com.example.domain.usecaae.GetAuthorsUseCase
import com.example.domain.usecaae.GetImagesUseCase
import com.example.domain.usecaae.LoadImagesUseCase
import com.example.domain.usecaae.impl.GetAuthorsUseCaseImpl
import com.example.domain.usecaae.impl.GetImagesUseCaseImpl
import com.example.domain.usecaae.impl.LoadImagesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class UseCaseModule {

    @Binds
    abstract fun bindGetAuthorsUseCase(getAuthorsUseCaseImpl: GetAuthorsUseCaseImpl): GetAuthorsUseCase

    @Binds
    abstract fun bindGetImagesUseCase(getImagesUseCaseImpl: GetImagesUseCaseImpl): GetImagesUseCase

    @Binds
    abstract fun bindLoadImagesUseCase(loadImagesUseCaseImpl: LoadImagesUseCaseImpl): LoadImagesUseCase
}