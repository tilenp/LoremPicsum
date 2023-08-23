package com.example.core.hilt

import com.example.core.mapper.ErrorMapper
import com.example.core.mapper.Mapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class MapperModule {

    @Binds
    abstract fun bindErrorMapper(errorMapper: ErrorMapper): Mapper<Throwable?, String>
}