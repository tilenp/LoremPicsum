package com.example.images.preferences.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.images.image_list.model.MenuItem
import com.example.images.image_list.model.ImageListDropdownMenu
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

    override suspend fun storeFilter(menuItem: MenuItem) {
        dataStore.edit { preferences ->
            when (menuItem) {
                is MenuItem.Author -> preferences[PreferencesKeys.AUTHOR_FILTER] = menuItem.text
            }
        }
    }

    override suspend fun clearFilter(menu: ImageListDropdownMenu) {
        dataStore.edit { preferences ->
            when (menu) {
                is ImageListDropdownMenu.Filter.Author -> preferences.remove(PreferencesKeys.AUTHOR_FILTER)
            }
        }
    }

    override fun getFilter(): Flow<MenuItem?> {
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

    private fun mapFilter(preferences: Preferences): MenuItem? {
        return preferences[PreferencesKeys.AUTHOR_FILTER]?.let { text ->
            MenuItem.Author(text = text, isSelected = true)
        }
    }
}