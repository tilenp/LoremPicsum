package com.example.images.navigation.image_list

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageListStateFactory @Inject constructor() {

    fun create(data: ImageListData): ImageListState {
        return when {
            data.isLoading -> ImageListState.Loading
            data.images.isEmpty() && data.message != null -> ImageListState.Error
            data.images.isEmpty() -> ImageListState.NothingToShow
            else -> data(data)
        }
    }

    private fun data(data: ImageListData): ImageListState {
        return ImageListState.Data(
            filter = data.filter,
            images = data.images
        )
    }
}