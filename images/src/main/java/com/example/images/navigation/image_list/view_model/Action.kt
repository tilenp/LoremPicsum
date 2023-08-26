package com.example.images.navigation.image_list.view_model

import com.example.domain.model.Image
import com.example.images.navigation.image_list.model.FilterItem
import com.example.images.navigation.image_list.model.ImageListData
import com.example.images.navigation.image_list.model.ImageListFilter

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

    data class Content(
        val authors: List<String>,
        val images: List<Image>,
        val selectedAuthor: String?
    ) : Action {
        override fun map(data: ImageListData): ImageListData {
            if (data.isLoading) {
                return data
            }
            return data.copy(
                filter = ImageListFilter.Author.authorFilter(
                    authors = authors,
                    selectedAuthor = selectedAuthor
                ),
                images = images,
            )
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