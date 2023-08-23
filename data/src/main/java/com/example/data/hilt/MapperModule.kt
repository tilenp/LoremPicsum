package com.example.data.hilt

import com.example.core.mapper.Mapper
import com.example.data.mapper.ImageTableMapper
import com.example.data.model.dto.ImageDto
import com.example.data.model.table.ImageTable
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class MapperModule {

    @Binds
    abstract fun bindImageTableMapper(imageTableMapper: ImageTableMapper): Mapper<ImageDto, ImageTable?>
}