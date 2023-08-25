package com.example.images.navigation.image_list.model

import com.example.domain.model.Image

internal data class ImageListData(
    val isLoading: Boolean,
    val filter: ImageListFilter,
    val images: List<Image>?,
    val errorMessage: String?,
) {
    companion object {
        val INITIAL = ImageListData(
            isLoading = false,
            filter = ImageListFilter.Author.INITIAL,
            images = null,
            errorMessage = null
        )
    }
}