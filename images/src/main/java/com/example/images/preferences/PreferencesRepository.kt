package com.example.images.preferences

import com.example.images.image_list.model.MenuItem
import com.example.images.image_list.model.ImageListDropdownMenu
import kotlinx.coroutines.flow.Flow

internal interface PreferencesRepository {
    suspend fun storeFilter(menuItem: MenuItem)
    suspend fun clearFilter(filter: ImageListDropdownMenu)
    fun getFilter(): Flow<MenuItem?>
}