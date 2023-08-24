package com.example.domain.usecaae.impl

import com.example.data.repository.ImageRepository
import com.example.domain.usecaae.GetAuthorsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetAuthorsUseCaseImpl @Inject constructor(
    private val imageRepository: ImageRepository,
) : GetAuthorsUseCase {

    override fun invoke(): Flow<List<String>> {
        return imageRepository.getAuthors()
    }
}