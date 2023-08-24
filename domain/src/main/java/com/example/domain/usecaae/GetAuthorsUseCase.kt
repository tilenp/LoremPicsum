package com.example.domain.usecaae

import kotlinx.coroutines.flow.Flow

interface GetAuthorsUseCase {
    operator fun invoke(): Flow<List<String>>
}