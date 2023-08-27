package com.example.images.image_list.model

internal sealed interface ImageListFilter {
    fun expand(expand: Boolean): ImageListFilter
    fun collapseIfContainsItem(filterItem: FilterItem): ImageListFilter
    data class Author private constructor(
        val expanded: Boolean,
        val items: List<FilterItem>,
    ) : ImageListFilter {
        override fun expand(expand: Boolean): ImageListFilter {
            return copy(expanded = expand)
        }

        override fun collapseIfContainsItem(filterItem: FilterItem): ImageListFilter {
            return if (items.contains(filterItem)) {
                copy(expanded = false)
            } else {
                this
            }
        }

        companion object {
            val INITIAL = Author(expanded = false, items = emptyList())

            fun buildFilter(items: List<String>, selectedItem: FilterItem?): ImageListFilter {
                val authorItems = items.map { author ->
                    FilterItem.Author(text = author, isSelected = author == selectedItem?.text)
                }
                return Author(expanded = false, items = authorItems)
            }
        }
    }
}

internal sealed interface FilterItem {
    val text: String
    val isSelected: Boolean

    data class Author(
        override val text: String,
        override val isSelected: Boolean
    ) : FilterItem
}