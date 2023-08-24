package com.example.images.navigation.image_list.model

internal sealed interface ImageListFilter {
    fun expand(expand: Boolean): ImageListFilter
    fun applyFilter(selectedItem: FilterItem): ImageListFilter
    fun clear(): ImageListFilter
    data class Author(
        val expanded: Boolean,
        val items: List<FilterItem>,
    ) : ImageListFilter {
        override fun expand(expand: Boolean): ImageListFilter {
            return copy(expanded = expand)
        }

        override fun applyFilter(selectedItem: FilterItem): ImageListFilter {
            return copy(items = items.map { item -> item.copy(isSelected = item.text == selectedItem.text) })
        }

        override fun clear(): Author {
            return copy(items = items.map { item -> item.copy(isSelected = false) })
        }
    }
}

internal data class FilterItem(
    val text: String,
    val isSelected: Boolean
)