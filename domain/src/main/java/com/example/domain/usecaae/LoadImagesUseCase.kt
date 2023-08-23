package com.example.domain.usecaae

import kotlinx.coroutines.flow.Flow

interface LoadImagesUseCase {
    operator fun invoke(): Flow<String>
}