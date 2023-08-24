package com.example.images.navigation.preferences.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.images.navigation.image_list.model.FilterItem
import com.example.images.navigation.preferences.PreferencesRepository
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

    override suspend fun storeFilter(filterItem: FilterItem?) {
        dataStore.edit { preferences ->
            when {
                filterItem != null -> preferences[PreferencesKeys.AUTHOR_FILTER] = filterItem.text
                else -> preferences.remove(PreferencesKeys.AUTHOR_FILTER)
            }
        }
    }

    override fun getFilter(): Flow<String?> {
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

    private fun mapFilter(preferences: Preferences): String? {
        return preferences[PreferencesKeys.AUTHOR_FILTER]
    }
}