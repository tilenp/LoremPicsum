package com.example.images.image_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.DispatcherProvider
import com.example.domain.usecaae.GetAuthorsUseCase
import com.example.domain.usecaae.GetImagesUseCase
import com.example.images.image_list.model.MenuItem
import com.example.images.image_list.model.ImageListData
import com.example.images.image_list.model.ImageListDropdownMenu
import com.example.images.image_list.model.ImageListState
import com.example.images.preferences.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
internal class ImageListViewModel @Inject constructor(
    private val taskBuilder: TaskBuilder,
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val getImagesUseCase: GetImagesUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val stateFactory: ImageListStateFactory,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val taskDispatcher = MutableSharedFlow<Task>(replay = 1)
    val state = merge(
        tasksFlow(),
        filtersFlow(),
        imagesFlow()
    )
        .scan(initial = ImageListData.INITIAL) { data, action -> action.apply(data = data) }
        .map { data -> stateFactory.create(data = data) }
        .stateIn(
            initialValue = ImageListState.Ignore,
            scope = viewModelScope.plus(dispatcherProvider.io),
            started = SharingStarted.WhileSubscribed(5000)
        )

    private fun tasksFlow(): Flow<Action> {
        return taskDispatcher.flatMapConcat { task -> task.actions }
    }

    private fun filtersFlow(): Flow<Action> {
        return getAuthorsUseCase.invoke()
            .flatMapLatest { authors ->
                preferencesRepository.getFilter()
                    .map { selectedItem ->
                        Action.ShowFilters(
                            items = authors,
                            selectedItem = selectedItem,
                        )
                    }
            }
    }

    private fun imagesFlow(): Flow<Action> {
        return preferencesRepository.getFilter()
            .flatMapLatest { selectedFilter ->
                getImagesUseCase.invoke(author = selectedFilter?.text)
                    .map { images -> Action.ShowImages(images = images) }
            }
    }

    init {
        loadImages()
    }

    fun loadImages() {
        viewModelScope.launch(dispatcherProvider.main) {
            taskDispatcher.emit(taskBuilder.loadImages())
        }
    }

    fun expandDropdownMenu(menu: ImageListDropdownMenu) {
        viewModelScope.launch(dispatcherProvider.main) {
            taskDispatcher.emit(taskBuilder.expandDropdownMenu(menu = menu))
        }
    }

    fun applySelection(menuItem: MenuItem) {
        viewModelScope.launch(dispatcherProvider.main) {
            taskDispatcher.emit(taskBuilder.applySelection(menuItem = menuItem))
        }
    }

    fun collapseDropdownMenu(filter: ImageListDropdownMenu) {
        viewModelScope.launch(dispatcherProvider.main) {
            taskDispatcher.emit(taskBuilder.collapseDropdownMenu(filter = filter))
        }
    }

    fun clearSelection(menu: ImageListDropdownMenu) {
        viewModelScope.launch(dispatcherProvider.main) {
            taskDispatcher.emit(taskBuilder.clearSelection(menu = menu))
        }
    }
}