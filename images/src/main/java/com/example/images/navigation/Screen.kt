package com.example.images.navigation

internal sealed class Screen(val route: String){
    object ImageList: Screen("ImageList")
}