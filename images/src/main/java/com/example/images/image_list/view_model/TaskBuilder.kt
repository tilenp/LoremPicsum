package com.example.images.image_list.view_model

import com.example.domain.usecaae.LoadImagesUseCase
import com.example.images.image_list.model.MenuItem
import com.example.images.image_list.model.ImageListDropdownMenu
import com.example.images.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TaskBuilder @Inject constructor(
    private val loadImagesUseCase: LoadImagesUseCase,
    private val preferencesRepository: PreferencesRepository,
) {

    fun loadImages(): Task {
        return Task(
            actions = loadImagesUseCase.invoke()
                .map { message -> Action.ShowErrorMessage(errorMessage = message) as Action }
                .onStart {
                    emit(Action.ClearErrorMessage)
                    emit(Action.Loading)
                }.onCompletion { emit(Action.NotLoading) }
        )
    }

    fun expandDropdownMenu(menu: ImageListDropdownMenu): Task {
        return Task(
            actions = flowOf(Action.ExpandFilter(filter = menu))
        )
    }

    fun applySelection(menuItem: MenuItem): Task {
        return Task(
            actions = flow<Action> { preferencesRepository.storeFilter(menuItem = menuItem) }
                .onStart {
                    emit(Action.CollapseFilterWithItem(menuItem = menuItem))
                    emit(Action.Loading)
                }.onCompletion { emit(Action.NotLoading) }
        )
    }

    fun collapseDropdownMenu(filter: ImageListDropdownMenu): Task {
        return Task(
            actions = flowOf(Action.ExpandFilter(filter = filter))
        )
    }

    fun clearSelection(menu: ImageListDropdownMenu): Task {
        return Task(
            actions = flow<Action> { preferencesRepository.clearFilter(filter = menu) }
                .onStart { emit(Action.Loading) }
                .onCompletion { emit(Action.NotLoading) }
        )
    }
}

internal data class Task(
    val actions: Flow<Action>
)