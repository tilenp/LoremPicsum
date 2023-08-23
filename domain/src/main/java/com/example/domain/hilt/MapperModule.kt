package com.example.domain.hilt

import com.example.core.mapper.Mapper
import com.example.data.model.table.ImageTable
import com.example.domain.mapper.ImageMapper
import com.example.domain.model.Image
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class MapperModule {

    @Binds
    abstract fun bindImageMapper(imageMapper: ImageMapper): Mapper<ImageTable, Image>
}

