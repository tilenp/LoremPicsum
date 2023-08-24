package com.example.data.repository

import com.example.data.model.table.ImageTable
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun loadImages()

    fun getAuthors(): Flow<List<String>>

    fun getImages(): Flow<List<ImageTable>>

    fun getImagesForAuthor(author: String): Flow<List<ImageTable>>
}