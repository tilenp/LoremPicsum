package com.example.images.navigation.image_list.model

import com.example.domain.model.Image

internal sealed interface ImageListState {
    object Loading: ImageListState
    data class Data(
        val filter: ImageListFilter,
        val images: List<Image>,
        val snackbarMessage: String?,
    ): ImageListState
    object NothingToShow: ImageListState
    data class Error(
        val message: String,
    ): ImageListState

    object Ignore: ImageListState
}