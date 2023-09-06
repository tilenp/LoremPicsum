package com.example.images.image_list.model

internal sealed interface ImageListDropdownMenu {
    fun expand(expand: Boolean): ImageListDropdownMenu
    fun collapseIfContainsItem(menuItem: MenuItem): ImageListDropdownMenu

    sealed interface Filter: ImageListDropdownMenu {

        data class Author private constructor(
            val expanded: Boolean,
            val items: List<MenuItem>,
        ) : Filter {
            override fun expand(expand: Boolean): ImageListDropdownMenu {
                return copy(expanded = expand)
            }

            override fun collapseIfContainsItem(menuItem: MenuItem): ImageListDropdownMenu {
                return if (items.contains(menuItem)) {
                    copy(expanded = false)
                } else {
                    this
                }
            }

            companion object {
                val INITIAL = Author(expanded = false, items = emptyList())

                fun buildFilter(items: List<String>, selectedItem: MenuItem?): ImageListDropdownMenu {
                    val authorItems = items.map { author ->
                        MenuItem.Author(text = author, isSelected = author == selectedItem?.text)
                    }
                    return Author(expanded = false, items = authorItems)
                }
            }
        }

    }
}

internal sealed interface MenuItem {
    val text: String
    val isSelected: Boolean

    data class Author(
        override val text: String,
        override val isSelected: Boolean
    ) : MenuItem
}