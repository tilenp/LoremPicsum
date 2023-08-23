package com.example.domain.mapper

import com.example.core.mapper.Mapper
import com.example.data.model.table.ImageTable
import com.example.domain.model.Image
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageMapper @Inject constructor() : Mapper<ImageTable, Image> {

    override fun map(objectToMap: ImageTable): Image {
        return Image(
            id = objectToMap.id,
            author = objectToMap.author,
            downloadUrl = objectToMap.downloadUrl,
        )
    }
}