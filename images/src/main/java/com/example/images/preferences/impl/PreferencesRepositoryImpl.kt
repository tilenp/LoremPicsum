package com.example.images.preferences.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.images.image_list.model.FilterItem
import com.example.images.image_list.model.ImageListFilter
import com.example.images.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferencesRepository {

    private object PreferencesKeys {
        val AUTHOR_FILTER = stringPreferencesKey("AUTHOR")
    }

    override suspend fun storeFilter(filterItem: FilterItem) {
        dataStore.edit { preferences ->
            when (filterItem) {
                is FilterItem.Author -> preferences[PreferencesKeys.AUTHOR_FILTER] = filterItem.text
            }
        }
    }

    override suspend fun clearFilter(filter: ImageListFilter) {
        dataStore.edit { preferences ->
            when (filter) {
                is ImageListFilter.Author -> preferences.remove(PreferencesKeys.AUTHOR_FILTER)
            }
        }
    }

    override fun getFilter(): Flow<FilterItem?> {
        return dataStore.data
            .catch { exception -> emit(handleReadException(throwable = exception)) }
            .map { preferences -> mapFilter(preferences = preferences) }
    }

    private fun handleReadException(throwable: Throwable): Preferences {
        return when (throwable) {
            is IOException -> emptyPreferences()
            else -> throw throwable
        }
    }

    private fun mapFilter(preferences: Preferences): FilterItem? {
        return preferences[PreferencesKeys.AUTHOR_FILTER]?.let { text ->
            FilterItem.Author(text = text, isSelected = true)
        }
    }
}