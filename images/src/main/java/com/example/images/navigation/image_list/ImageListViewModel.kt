package com.example.images.navigation.image_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.DispatcherProvider
import com.example.domain.model.Image
import com.example.domain.usecaae.GetAuthorsUseCase
import com.example.domain.usecaae.GetImagesUseCase
import com.example.domain.usecaae.LoadImagesUseCase
import com.example.images.navigation.image_list.model.FilterItem
import com.example.images.navigation.image_list.model.ImageListData
import com.example.images.navigation.image_list.model.ImageListFilter
import com.example.images.navigation.image_list.model.ImageListFilter.Author.Companion.authorFilter
import com.example.images.navigation.image_list.model.ImageListState
import com.example.images.navigation.preferences.PreferencesRepository
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

    private val eventDispatcher = MutableSharedFlow<Event>()
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

    private sealed interface Event {
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
                    .onStart { emit(Action.Loading) }
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

    private sealed interface Action {
        fun map(data: ImageListData): ImageListData

        object Loading : Action {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(isLoading = true)
            }
        }

        object NotLoading : Action {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(isLoading = false)
            }
        }

        data class Content(
            val authors: List<String>,
            val images: List<Image>,
            val selectedAuthor: String?
        ) : Action {
            override fun map(data: ImageListData): ImageListData {
                if (data.isLoading) {
                    return data
                }
                return data.copy(
                    filter = authorFilter(authors = authors, selectedAuthor = selectedAuthor),
                    images = images,
                )
            }
        }

        data class ExpandFilter(val filter: ImageListFilter) : Action {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(filter = filter.expand(true))
            }
        }

        data class CollapseFilter(val filter: ImageListFilter) : Action {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(filter = filter.expand(false))
            }
        }

        data class ShowErrorMessage(val errorMessage: String) : Action {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(errorMessage = errorMessage)
            }
        }

        object ClearErrorMessage : Action {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(errorMessage = null)
            }
        }
    }
}