package com.example.images.image_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.DispatcherProvider
import com.example.domain.usecaae.GetAuthorsUseCase
import com.example.domain.usecaae.GetImagesUseCase
import com.example.domain.usecaae.LoadImagesUseCase
import com.example.images.image_list.model.FilterItem
import com.example.images.image_list.model.ImageListData
import com.example.images.image_list.model.ImageListFilter
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
    private val loadImagesUseCase: LoadImagesUseCase,
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val getImagesUseCase: GetImagesUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val stateFactory: ImageListStateFactory,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val eventDispatcher = MutableSharedFlow<Event>(replay = 1)
    val state = merge(
        eventDispatcher.flatMapLatest { event -> event.execute() },
        contentFlow()
    )
        .scan(initial = ImageListData.INITIAL) { data, action -> action.map(data = data) }
        .map { data -> stateFactory.create(data = data) }
        .stateIn(
            initialValue = ImageListState.Ignore,
            scope = viewModelScope.plus(dispatcherProvider.io),
            started = SharingStarted.WhileSubscribed(5000)
        )

    private fun contentFlow(): Flow<Action> {
        return preferencesRepository.getFilter()
            .flatMapLatest { selectedAuthor ->
                combine(
                    getAuthorsUseCase.invoke(),
                    getImagesUseCase.invoke(author = selectedAuthor)
                ) { authors, images ->
                    Action.Content(
                        authors = authors,
                        images = images,
                        selectedAuthor = selectedAuthor
                    )
                }
            }
    }

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(dispatcherProvider.main) {
            eventDispatcher.emit(Event.Refresh(loadImages = loadImagesUseCase.invoke()))
        }
    }

    fun expandFilter(filter: ImageListFilter) {
        viewModelScope.launch(dispatcherProvider.main) {
            eventDispatcher.emit(Event.ExpandFilter(filter = filter))
        }
    }

    fun applyFilter(filterItem: FilterItem) {
        viewModelScope.launch(dispatcherProvider.main) {
            eventDispatcher.emit(
                Event.ApplyFilter(
                    filterItem = filterItem,
                    preferencesRepository = preferencesRepository
                )
            )
        }
    }

    fun collapseFilter(filter: ImageListFilter) {
        viewModelScope.launch(dispatcherProvider.main) {
            eventDispatcher.emit(Event.CollapseFilter(filter = filter))
        }
    }

    fun clearFilter(filter: ImageListFilter) {
        viewModelScope.launch(dispatcherProvider.main) {
            eventDispatcher.emit(
                Event.ClearFilter(
                    filter = filter,
                    preferencesRepository = preferencesRepository
                )
            )
        }
    }
}