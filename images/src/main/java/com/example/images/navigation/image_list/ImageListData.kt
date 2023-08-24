package com.example.images.navigation.image_list

import com.example.domain.model.Image

internal data class ImageListData(
    val isLoading: Boolean,
    val filter: ImageListFilter?,
    val images: List<Image>,
    val message: String?,
) {
    companion object {
        val INITIAL = ImageListData(
            isLoading = false,
            filter = null,
            images = emptyList(),
            message = null
        )
    }
}