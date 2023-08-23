package com.example.domain.usecaae.impl

import com.example.core.mapper.Mapper
import com.example.data.model.table.ImageTable
import com.example.data.repository.ImageRepository
import com.example.domain.model.Image
import com.example.domain.usecaae.GetImagesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetImagesUseCaseImpl @Inject constructor(
    private val imageRepository: ImageRepository,
    private val imageMapper: Mapper<ImageTable, Image>
) : GetImagesUseCase {

    override fun invoke(author: String?): Flow<List<Image>> {
        return when {
            author.isNullOrBlank() -> imageRepository.getImages().toImageFlow()
            else -> imageRepository.getImagesForAuthor(author = author).toImageFlow()
        }
    }

    private fun Flow<List<ImageTable>>.toImageFlow(): Flow<List<Image>> {
        return this.map { list -> list.map { imageTable -> imageMapper.map(imageTable) } }
    }
}