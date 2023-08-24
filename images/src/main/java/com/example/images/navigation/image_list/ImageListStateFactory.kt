package com.example.images.navigation.image_list

import com.example.images.navigation.image_list.model.ImageListData
import com.example.images.navigation.image_list.model.ImageListState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageListStateFactory @Inject constructor() {

    fun create(data: ImageListData): ImageListState {
        return when {
            data.isLoading -> ImageListState.Loading
            data.images.isEmpty() && data.errorMessage != null -> error(message = data.errorMessage)
            data.images.isEmpty() -> ImageListState.NothingToShow
            else -> data(data)
        }
    }

    private fun error(message: String): ImageListState {
        return ImageListState.Error(message = message)
    }

    private fun data(data: ImageListData): ImageListState {
        return ImageListState.Data(
            filter = data.filter,
            images = data.images,
            snackbarMessage = data.errorMessage
        )
    }
}