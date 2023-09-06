package com.example.images.image_list.view_model

import com.example.domain.model.Image
import com.example.images.image_list.model.MenuItem
import com.example.images.image_list.model.ImageListData
import com.example.images.image_list.model.ImageListDropdownMenu
import com.example.images.image_list.model.ImageListDropdownMenu.Filter.Author.Companion.buildFilter

internal sealed interface Action {
    fun apply(data: ImageListData): ImageListData

    object Loading : Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(isLoading = true)
        }
    }

    object NotLoading : Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(isLoading = false)
        }
    }

    data class ShowFilters(
        val items: List<String>,
        val selectedItem: MenuItem?
    ): Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(
                filter = buildFilter(
                    items = items,
                    selectedItem = selectedItem
                )
            )
        }
    }

    data class ShowImages(val images: List<Image>) : Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(images = images)
        }
    }

    data class ExpandFilter(val filter: ImageListDropdownMenu) : Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(filter = filter.expand(true))
        }
    }

    data class CollapseFilter(val filter: ImageListDropdownMenu) : Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(filter = filter.expand(false))
        }
    }

    data class CollapseFilterWithItem(val menuItem: MenuItem) : Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(filter = data.filter.collapseIfContainsItem(menuItem = menuItem))
        }
    }

    data class ShowErrorMessage(val errorMessage: String) : Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(errorMessage = errorMessage)
        }
    }

    object ClearErrorMessage : Action {
        override fun apply(data: ImageListData): ImageListData {
            return data.copy(errorMessage = null)
        }
    }
}