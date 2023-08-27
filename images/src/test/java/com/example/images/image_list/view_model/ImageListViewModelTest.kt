@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.images.image_list.view_model

import app.cash.turbine.test
import com.example.domain.model.Image
import com.example.domain.usecaae.GetAuthorsUseCase
import com.example.domain.usecaae.GetImagesUseCase
import com.example.domain.usecaae.LoadImagesUseCase
import com.example.images.FakeDispatcherProvider
import com.example.images.image_list.model.FilterItem
import com.example.images.image_list.model.ImageListFilter.Author.Companion.authorFilter
import com.example.images.image_list.model.ImageListState
import com.example.images.preferences.PreferencesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ImageListViewModelTest {

    private lateinit var viewModel: ImageListViewModel
    private val loadImagesUseCase: LoadImagesUseCase = mockk()
    private val getAuthorsUseCase: GetAuthorsUseCase = mockk()
    private val getImagesUseCase: GetImagesUseCase = mockk()
    private val preferencesRepository: PreferencesRepository = mockk()
    private val testScope = TestScope()

    private val getFilterFlow = MutableSharedFlow<String?>(replay = 1)
    private val getAuthorsFlow = MutableSharedFlow<List<String>>(replay = 1)
    private val getImagesFlow = MutableSharedFlow<List<Image>>(replay = 1)
    private val image1 = Image(
        id = "1",
        author = "author1",
        downloadUrl = "downloadUrl1"
    )
    private val image2 = Image(
        id = "2",
        author = "author 2",
        downloadUrl = "downloadUrl 2"
    )

    private fun setUp() {
        coEvery { preferencesRepository.storeFilter(any()) } returns Unit
        every { preferencesRepository.getFilter() } returns getFilterFlow
        every { getAuthorsUseCase.invoke() } returns getAuthorsFlow
        every { getImagesUseCase.invoke(any()) } returns getImagesFlow

        viewModel = ImageListViewModel(
            loadImagesUseCase = loadImagesUseCase,
            getAuthorsUseCase = getAuthorsUseCase,
            getImagesUseCase = getImagesUseCase,
            preferencesRepository = preferencesRepository,
            stateFactory = ImageListStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(
                testDispatcher = UnconfinedTestDispatcher(testScope.testScheduler)
            )
        )
    }

    @Test
    fun `content loaded successfully, response contains images`() = runTest {
        every { loadImagesUseCase.invoke() } returns emptyFlow()
        setUp()

        viewModel.state.test {

            // load images -> requests succeeds
            assertEquals(ImageListState.Loading, awaitItem())

            // DataStore emits null -> no filters
            getFilterFlow.emit(null)
            // database queries authors
            coVerify(exactly = 1) { getAuthorsUseCase.invoke() }
            // database queries images
            coVerify(exactly = 1) { getImagesUseCase.invoke(author = null) }

            // database emits authors
            getAuthorsFlow.emit(listOf("author 1", "author 2"))
            // database emits images
            getImagesFlow.emit(listOf(image1, image2))

            // show content
            assertEquals(
                ImageListState.Content(
                    filter = authorFilter(
                        authors = listOf("author 1", "author 2"),
                        selectedAuthor = null
                    ),
                    images = listOf(image1, image2),
                    snackbarMessage = null
                ),
                awaitItem()
            )
        }
    }

    @Test
    fun `content loaded successfully, response returns empty list`() = runTest {
        every { loadImagesUseCase.invoke() } returns emptyFlow()
        setUp()

        viewModel.state.test {

            // load images -> requests succeeds
            assertEquals(ImageListState.Loading, awaitItem())

            // DataStore emits null -> no filters
            getFilterFlow.emit(null)
            // database queries authors
            coVerify(exactly = 1) { getAuthorsUseCase.invoke() }
            // database queries images
            coVerify(exactly = 1) { getImagesUseCase.invoke(author = null) }

            // database emits empty authors list
            getAuthorsFlow.emit(emptyList())
            // database emits empty images list
            getImagesFlow.emit(emptyList())

            // show NothingToShow state
            assertEquals(
                ImageListState.NothingToShow,
                awaitItem()
            )
        }
    }

    @Test
    fun `content load failed and database is empty`() = runTest {
        every { loadImagesUseCase.invoke() } returns flowOf("network error")
        setUp()

        viewModel.state.test {

            // load images -> request throws an error
            assertEquals(ImageListState.Loading, awaitItem())

            // DataStore emits null -> no filters
            getFilterFlow.emit(null)
            // database queries authors
            coVerify(exactly = 1) { getAuthorsUseCase.invoke() }
            // database queries images
            coVerify(exactly = 1) { getImagesUseCase.invoke(author = null) }

            // database emits empty authors list
            getAuthorsFlow.emit(emptyList())
            // database emits empty images list
            getImagesFlow.emit(emptyList())

            // show error state
            assertEquals(
                ImageListState.Error("network error"),
                awaitItem()
            )
        }
    }

    @Test
    fun `content load failed and database is not empty`() = runTest {
        every { loadImagesUseCase.invoke() } returns flowOf("network error")
        setUp()

        viewModel.state.test {

            // load images -> request throws an error
            assertEquals(ImageListState.Loading, awaitItem())

            // DataStore emits null -> no filters
            getFilterFlow.emit(null)
            // database queries authors
            coVerify(exactly = 1) { getAuthorsUseCase.invoke() }
            // database queries images
            coVerify(exactly = 1) { getImagesUseCase.invoke(author = null) }

            // database emits authors
            getAuthorsFlow.emit(listOf("author 1", "author 2"))
            // database emits images
            getImagesFlow.emit(listOf(image1, image2))

            // show content state with an error snackbar
            assertEquals(
                ImageListState.Content(
                    filter = authorFilter(
                        authors = listOf("author 1", "author 2"),
                        selectedAuthor = null
                    ),
                    images = listOf(image1, image2),
                    snackbarMessage = "network error"
                ),
                awaitItem()
            )
        }
    }

    @Test
    fun `apply filter flow`() = runTest {
        every { loadImagesUseCase.invoke() } returns emptyFlow()
        val filter = authorFilter(
            authors = listOf("author 1", "author 2"),
            selectedAuthor = null
        )
        val filterItem = FilterItem.Author(text = "author 1", isSelected = false)
        setUp()

        viewModel.state.test {

            // load images -> requests succeeds
            assertEquals(ImageListState.Loading, awaitItem())

            // DataStore emits null -> no filters
            getFilterFlow.emit(null)
            // database queries authors
            coVerify(exactly = 1) { getAuthorsUseCase.invoke() }
            // database queries images
            coVerify(exactly = 1) { getImagesUseCase.invoke(author = null) }

            // database emits authors
            getAuthorsFlow.emit(listOf("author 1", "author 2"))
            // database emits images
            getImagesFlow.emit(listOf(image1, image2))

            // show content
            assertEquals(
                ImageListState.Content(
                    filter = filter,
                    images = listOf(image1, image2),
                    snackbarMessage = null
                ),
                awaitItem()
            )

            // expand dropdown
            viewModel.expandFilter(filter = filter)

            // show content with expanded dropdown
            assertEquals(
                ImageListState.Content(
                    filter = filter.expand(true),
                    images = listOf(image1, image2),
                    snackbarMessage = null
                ),
                awaitItem()
            )

            // apply filter
            viewModel.applyFilter(filterItem = filterItem)

            // show content with collapsed dropdown
            assertEquals(
                ImageListState.Content(
                    filter = filter.expand(false),
                    images = listOf(image1, image2),
                    snackbarMessage = null
                ),
                awaitItem()
            )

            // filter gets stored into DataStore
            coVerify(exactly = 1) { preferencesRepository.storeFilter(filterItem = filterItem) }

            // DataStore emits selected filter
            getFilterFlow.emit("author 1")

            // show content with selected filter
            assertEquals(
                ImageListState.Content(
                    filter = authorFilter(
                        authors = listOf("author 1", "author 2"),
                        selectedAuthor = "author 1"
                    ),
                    images = listOf(image1, image2),
                    snackbarMessage = null
                ),
                awaitItem()
            )

            // database queries authors for the 2nd time
            coVerify(exactly = 2) { getAuthorsUseCase.invoke() }
            // database queries filtered images
            coVerify(exactly = 1) { getImagesUseCase.invoke(author = "author 1") }

            // database emits authors
            getAuthorsFlow.emit(listOf("author 1", "author 2"))
            // database emits filtered images
            getImagesFlow.emit(listOf(image1))

            // show content with filtered images
            assertEquals(
                ImageListState.Content(
                    filter = authorFilter(
                        authors = listOf("author 1", "author 2"),
                        selectedAuthor = "author 1"
                    ),
                    images = listOf(image1),
                    snackbarMessage = null
                ),
                awaitItem()
            )
        }
    }
}