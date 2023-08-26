package com.example.images.navigation.image_list.view_model

import com.example.images.navigation.image_list.model.ImageListData
import com.example.images.navigation.image_list.model.ImageListState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageListStateFactory @Inject constructor() {

    fun create(data: ImageListData): ImageListState {
        return showLoading(data = data)
            ?: showError(data = data)
            ?: showNothingToShow(data = data)
            ?: showContent(data = data)
            ?: ImageListState.Ignore
    }

    private fun showLoading(data: ImageListData): ImageListState? {
        return if (data.isLoading || data.images == null) {
            ImageListState.Loading
        } else {
            null
        }
    }

    private fun showError(data: ImageListData): ImageListState? {
        return if (data.images?.isEmpty() == true && data.errorMessage != null) {
            ImageListState.Error(message = data.errorMessage)
        } else {
            null
        }
    }

    private fun showNothingToShow(data: ImageListData): ImageListState? {
        return if (data.images?.isEmpty() == true) {
            ImageListState.NothingToShow
        } else {
            null
        }
    }

    private fun showContent(data: ImageListData): ImageListState? {
        return if (data.images != null) {
            ImageListState.Content(
                filter = data.filter,
                images = data.images,
                snackbarMessage = data.errorMessage
            )
        } else {
            null
        }
    }
}