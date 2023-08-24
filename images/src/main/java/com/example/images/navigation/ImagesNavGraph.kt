@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.images.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.images.navigation.image_list.views.ImageListScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun ImagesNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ImageList.route,
    ) {
        composable(route = Screen.ImageList.route) {
            ImageListScreen()
        }
    }
}