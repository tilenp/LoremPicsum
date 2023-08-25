package com.example.images.navigation.preferences

import com.example.images.navigation.image_list.model.FilterItem
import com.example.images.navigation.image_list.model.ImageListFilter
import kotlinx.coroutines.flow.Flow

internal interface PreferencesRepository {
    suspend fun storeFilter(filterItem: FilterItem)
    suspend fun clearFilter(filter: ImageListFilter)
    fun getFilter(): Flow<String?>
}