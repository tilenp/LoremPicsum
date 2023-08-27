package com.example.images.image_list.view_model

import com.example.domain.model.Image
import com.example.images.image_list.model.FilterItem
import com.example.images.image_list.model.ImageListData
import com.example.images.image_list.model.ImageListFilter
import com.example.images.image_list.model.ImageListFilter.Author.Companion.buildFilter

internal sealed interface Action {
    fun map(data: ImageListData): ImageListData

    object Loading : Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(isLoading = true)
        }
    }

    object NotLoading : Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(isLoading = false)
        }
    }

    data class ShowFilters(
        val items: List<String>,
        val selectedItem: FilterItem?
    ): Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(
                filter = buildFilter(
                    items = items,
                    selectedItem = selectedItem
                )
            )
        }
    }

    data class ShowImages(val images: List<Image>) : Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(images = images)
        }
    }

    data class ExpandFilter(val filter: ImageListFilter) : Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(filter = filter.expand(true))
        }
    }

    data class CollapseFilter(val filter: ImageListFilter) : Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(filter = filter.expand(false))
        }
    }

    data class CollapseFilterWithItem(val filterItem: FilterItem) : Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(filter = data.filter.collapseIfContainsItem(filterItem = filterItem))
        }
    }

    data class ShowErrorMessage(val errorMessage: String) : Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(errorMessage = errorMessage)
        }
    }

    object ClearErrorMessage : Action {
        override fun map(data: ImageListData): ImageListData {
            return data.copy(errorMessage = null)
        }
    }
}