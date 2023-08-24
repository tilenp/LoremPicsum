package com.example.data.repository.impl

import com.example.core.mapper.Mapper
import com.example.data.api.LoremPicsumApi
import com.example.data.database.dao.ImageDao
import com.example.data.model.dto.ImageDto
import com.example.data.model.table.ImageTable
import com.example.data.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageRepositoryImpl @Inject constructor(
    private val api: LoremPicsumApi,
    private val imageDao: ImageDao,
    private val imageTableMapper: Mapper<ImageDto, ImageTable?>,
) : ImageRepository {

    override suspend fun loadImages() {
        val imageDtoList = api.loadImages()
        val imageTableList = imageDtoList.map { imageDao -> imageTableMapper.map(imageDao) }
        imageDao.insertImages(images = imageTableList.filterNotNull())
    }

    override fun getAuthors(): Flow<List<String>> {
        return imageDao.getAuthors()
    }

    override fun getImages(): Flow<List<ImageTable>> {
        return imageDao.getImages()
    }

    override fun getImagesForAuthor(author: String): Flow<List<ImageTable>> {
        return imageDao.getImagesForAuthor(author = author)
    }
}