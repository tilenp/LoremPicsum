package com.example.images.preferences

import com.example.images.image_list.model.FilterItem
import com.example.images.image_list.model.ImageListFilter
import kotlinx.coroutines.flow.Flow

internal interface PreferencesRepository {
    suspend fun storeFilter(filterItem: FilterItem)
    suspend fun clearFilter(filter: ImageListFilter)
    fun getFilter(): Flow<FilterItem?>
}