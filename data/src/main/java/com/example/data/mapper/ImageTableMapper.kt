package com.example.data.mapper

import com.example.core.mapper.Mapper
import com.example.data.model.dto.ImageDto
import com.example.data.model.table.ImageTable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageTableMapper @Inject constructor() : Mapper<ImageDto, ImageTable?> {
    override fun map(objectToMap: ImageDto): ImageTable? {
        if (objectToMap.id.isNullOrBlank()) {
            return null
        }
        return ImageTable(
            id = objectToMap.id,
            author = objectToMap.author.orEmpty(),
            downloadUrl = objectToMap.downloadUrl.orEmpty(),
        )
    }
}