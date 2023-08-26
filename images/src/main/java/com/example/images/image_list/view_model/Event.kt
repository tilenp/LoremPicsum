package com.example.images.image_list.view_model

import com.example.images.image_list.model.FilterItem
import com.example.images.image_list.model.ImageListFilter
import com.example.images.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

internal sealed interface Event {
    fun execute(): Flow<Action>
    data class Refresh(val loadImages: Flow<String>) : Event {
        override fun execute(): Flow<Action> {
            return loadImages
                .map { message -> Action.ShowErrorMessage(errorMessage = message) as Action }
                .onStart {
                    emit(Action.ClearErrorMessage)
                    emit(Action.Loading)
                }.onCompletion { emit(Action.NotLoading) }
        }
    }

    data class ExpandFilter(val filter: ImageListFilter) : Event {
        override fun execute(): Flow<Action> {
            return flowOf(Action.ExpandFilter(filter = filter))
        }
    }

    data class ApplyFilter(
        val filterItem: FilterItem,
        val preferencesRepository: PreferencesRepository
    ) : Event {
        override fun execute(): Flow<Action> {
            return flow<Action> { preferencesRepository.storeFilter(filterItem = filterItem) }
                .onStart {
                    emit(Action.CollapseFilterWithItem(filterItem = filterItem))
                    emit(Action.Loading)
                }
                .onCompletion { emit(Action.NotLoading) }
        }
    }

    data class CollapseFilter(val filter: ImageListFilter) : Event {
        override fun execute(): Flow<Action> {
            return flowOf(Action.CollapseFilter(filter = filter))
        }
    }

    data class ClearFilter(
        val filter: ImageListFilter,
        val preferencesRepository: PreferencesRepository
    ) : Event {
        override fun execute(): Flow<Action> {
            return flow<Action> { preferencesRepository.clearFilter(filter = filter) }
                .onStart { emit(Action.Loading) }
                .onCompletion { emit(Action.NotLoading) }
        }
    }
}