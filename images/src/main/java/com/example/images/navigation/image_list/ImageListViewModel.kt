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

    private val actionDispatcher = MutableSharedFlow<Action>()
    val state = merge(
        getSingleActionFlow(),
        getMultiActionFlow(),
        getImagesFlow(),
    )
        .scan(initial = ImageListData.INITIAL) { data, action -> action.map(data = data) }
        .map { data -> stateFactory.create(data = data) }
        .stateIn(
            initialValue = ImageListState.Ignore,
            scope = viewModelScope.plus(dispatcherProvider.io),
            started = SharingStarted.WhileSubscribed(5000)
        )

    init {
        refresh()
    }

    private fun getSingleActionFlow(): Flow<SingleAction> {
        return actionDispatcher
            .filterIsInstance<SingleAction>()
    }

    private fun getMultiActionFlow(): Flow<Action> {
        return actionDispatcher
            .filterIsInstance<MultiAction>()
            .flatMapLatest {
                loadImagesUseCase.invoke()
                    .map { message -> SingleAction.ShowErrorMessage(errorMessage = message) as Action }
                    .onStart {
                        emit(SingleAction.ClearErrorMessage)
                        emit(SingleAction.Loading)
                    }
                    .onCompletion { emit(SingleAction.NotLoading) }
            }
    }

    private fun getImagesFlow(): Flow<SingleAction.Content> {
        return preferencesRepository.getFilter()
            .flatMapLatest { selectedAuthor ->
                combine(
                    getAuthorsUseCase.invoke(),
                    getImagesUseCase.invoke(author = selectedAuthor)
                ) { authors, images ->
                    SingleAction.Content(
                        authors = authors,
                        images = images,
                        selectedAuthor = selectedAuthor
                    )
                }
            }
    }

    fun refresh() {
        viewModelScope.launch(dispatcherProvider.main) {
            actionDispatcher.emit(MultiAction.Refresh)
        }
    }

    fun expandFilter(filter: ImageListFilter) {
        viewModelScope.launch(dispatcherProvider.main) {
            actionDispatcher.emit(SingleAction.ExpandFilter(filter = filter))
        }
    }

    fun collapseFilter(filter: ImageListFilter) {
        viewModelScope.launch(dispatcherProvider.main) {
            actionDispatcher.emit(SingleAction.CollapseFilter(filter = filter))
        }
    }

    fun applyFilter(filter: ImageListFilter, selectedItem: FilterItem) {
        viewModelScope.launch(dispatcherProvider.main) {
            preferencesRepository.storeFilter(filterItem = selectedItem)
            actionDispatcher.emit(SingleAction.CollapseFilter(filter = filter))
        }
    }

    fun clearFilter() {
        viewModelScope.launch(dispatcherProvider.main) {
            preferencesRepository.storeFilter(filterItem = null)
        }
    }

    private sealed interface Action {
        fun map(data: ImageListData): ImageListData
    }

    private sealed interface SingleAction : Action {
        object Loading : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(isLoading = true)
            }
        }

        object NotLoading : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(isLoading = false)
            }
        }

        data class Content(
            val authors: List<String>,
            val images: List<Image>,
            val selectedAuthor: String?
        ) : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                val items = authors.map { author ->
                    FilterItem(text = author, isSelected = author == selectedAuthor)
                }
                return data.copy(
                    filter = ImageListFilter.Author(expanded = false, items = items),
                    images = images,
                )
            }
        }

        data class ExpandFilter(val filter: ImageListFilter) : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(filter = filter.expand(true))
            }
        }

        data class CollapseFilter(val filter: ImageListFilter) : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(filter = filter.expand(false))
            }
        }

        data class ShowErrorMessage(val errorMessage: String) : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(errorMessage = errorMessage)
            }
        }

        object ClearErrorMessage : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(errorMessage = null)
            }
        }
    }

    private sealed interface MultiAction : Action {
        object Refresh : MultiAction {
            override fun map(data: ImageListData): ImageListData {
                return data
            }
        }
    }
}