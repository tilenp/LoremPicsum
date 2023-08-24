package com.example.images.navigation.preferences

import com.example.images.navigation.image_list.model.FilterItem
import kotlinx.coroutines.flow.Flow

internal interface PreferencesRepository {
    suspend fun storeFilter(filterItem: FilterItem?)
    fun getFilter(): Flow<String?>
}