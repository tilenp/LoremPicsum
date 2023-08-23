package com.example.domain.usecaae.impl

import com.example.core.mapper.Mapper
import com.example.data.repository.ImageRepository
import com.example.domain.usecaae.LoadImagesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LoadImagesUseCaseImpl @Inject constructor(
    private val imageRepository: ImageRepository,
    private val errorMapper: Mapper<Throwable?, String>
) : LoadImagesUseCase {

    override fun invoke(): Flow<String> {
        return flow<String> { imageRepository.loadImages() }
            .catch { throwable -> emit(errorMapper.map(throwable)) }
    }
}