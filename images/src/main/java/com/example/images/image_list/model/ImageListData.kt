package com.example.images.image_list.model

import com.example.domain.model.Image

internal data class ImageListData(
    val isLoading: Boolean,
    val filter: ImageListDropdownMenu,
    val images: List<Image>?,
    val errorMessage: String?,
) {
    companion object {
        val INITIAL = ImageListData(
            isLoading = false,
            filter = ImageListDropdownMenu.Filter.Author.INITIAL,
            images = null,
            errorMessage = null
        )
    }
}