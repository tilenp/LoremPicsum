package com.example.images.image_list.model

import com.example.domain.model.Image

internal sealed interface ImageListState {
    object Loading: ImageListState
    data class Content(
        val filter: ImageListDropdownMenu,
        val images: List<Image>,
        val snackbarMessage: String?,
    ): ImageListState
    object NothingToShow: ImageListState
    data class Error(
        val message: String,
    ): ImageListState

    object Ignore: ImageListState
}