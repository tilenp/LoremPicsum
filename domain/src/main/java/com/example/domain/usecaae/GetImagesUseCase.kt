package com.example.domain.usecaae

import com.example.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface GetImagesUseCase {
    operator fun invoke(author: String? = null): Flow<List<Image>>
}