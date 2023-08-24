package com.example.images.navigation.image_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.DispatcherProvider
import com.example.domain.model.Image
import com.example.domain.usecaae.GetImagesUseCase
import com.example.domain.usecaae.LoadImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
internal class ImageListViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val loadImagesUseCase: LoadImagesUseCase,
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
            initialValue = ImageListState.NothingToShow,
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
                    .map { message -> SingleAction.ShowMessage(message = message) as Action }
                    .onStart { emit(SingleAction.Loading) }
                    .onCompletion { emit(SingleAction.NotLoading) }
            }
    }

    private fun getImagesFlow(): Flow<SingleAction.Images> {
        return getImagesUseCase.invoke()
            .map { images -> SingleAction.Images(images = images) }
    }

    fun refresh() {
        viewModelScope.launch(dispatcherProvider.main) {
            actionDispatcher.emit(MultiAction.Refresh)
        }
    }

    fun filterByAuthor(author: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            actionDispatcher.emit(SingleAction.FilterByAuthor(author = author))
        }
    }

    fun clearFilters() {
        viewModelScope.launch(dispatcherProvider.main) {
            actionDispatcher.emit(SingleAction.ClearFilters)
        }
    }

    fun clearMessage() {
        viewModelScope.launch(dispatcherProvider.main) {
            actionDispatcher.emit(SingleAction.ClearMessage)
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

        data class Images(val images: List<Image>) : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(images = images)
            }
        }

        data class FilterByAuthor(val author: String) : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(filter = ImageListFilter.Author(author = author))
            }
        }

        object ClearFilters : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(filter = null)
            }
        }

        data class ShowMessage(val message: String) : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(message = message)
            }
        }

        object ClearMessage : SingleAction {
            override fun map(data: ImageListData): ImageListData {
                return data.copy(message = null)
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