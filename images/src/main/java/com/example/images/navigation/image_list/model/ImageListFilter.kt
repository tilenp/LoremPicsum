package com.example.images.navigation.image_list.model

internal sealed interface ImageListFilter {
    fun expand(expand: Boolean): ImageListFilter
    data class Author(
        val expanded: Boolean,
        val items: List<FilterItem>,
    ) : ImageListFilter {
        override fun expand(expand: Boolean): ImageListFilter {
            return copy(expanded = expand)
        }

        companion object {
            val INITIAL = Author(expanded = false, items = emptyList())
        }
    }
}

internal sealed interface FilterItem {
    val text: String
    val isSelected: Boolean

    data class Author(
        override val text: String,
        override val isSelected: Boolean
    ): FilterItem
}