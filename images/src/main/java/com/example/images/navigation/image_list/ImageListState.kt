package com.example.images.navigation.image_list

import com.example.domain.model.Image

internal sealed interface ImageListState {
    object Loading: ImageListState
    data class Data(
        val filter: ImageListFilter?,
        val images: List<Image>,
    ): ImageListState
    object NothingToShow: ImageListState
    object Error: ImageListState
}