package com.example.images.navigation.image_list

internal sealed interface ImageListFilter {
    data class Author(
        val author: String,
    ): ImageListFilter
}