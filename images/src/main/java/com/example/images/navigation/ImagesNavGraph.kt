package com.example.images.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun ImagesNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ImageList.route,
    ) {
        composable(route = Screen.ImageList.route) {
            Text(text = "Hello!")
        }
    }
}