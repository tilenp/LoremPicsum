@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)

package com.example.images.image_list.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.ui.theme.Dimens
import com.example.domain.model.Image
import com.example.images.common.DropdownView
import com.example.images.common.LoadingView
import com.example.images.common.MessageView
import com.example.images.common.MyButton
import com.example.images.common.MySnackbar
import com.example.images.image_list.model.MenuItem
import com.example.images.image_list.model.ImageListDropdownMenu
import com.example.images.image_list.model.ImageListState
import com.example.images.image_list.view_model.ImageListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.core.R as CoreR
import com.example.images.R as ImagesR

@Composable
internal fun ImageListScreen(
    viewModel: ImageListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ImageListScreen(
        state = state,
        onDropdownClick = viewModel::expandDropdownMenu,
        onItemClick = viewModel::applySelection,
        onDismissRequest = viewModel::collapseDropdownMenu,
        onClearFilterClick = viewModel::clearSelection,
        onRetryClick = viewModel::loadImages,
    )
}

@Composable
private fun ImageListScreen(
    state: ImageListState,
    onDropdownClick: (ImageListDropdownMenu) -> Unit,
    onItemClick: (MenuItem) -> Unit,
    onDismissRequest: (ImageListDropdownMenu) -> Unit,
    onClearFilterClick: (ImageListDropdownMenu) -> Unit,
    onRetryClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is ImageListState.Loading -> LoadingView(
                    modifier = Modifier.fillMaxSize()
                )

                is ImageListState.Content -> ContentView(
                    state = state,
                    onDropdownClick = onDropdownClick,
                    onItemClick = onItemClick,
                    onDismissRequest = onDismissRequest,
                    onClearFilterClick = onClearFilterClick,
                    snackbarHostState = snackbarHostState,
                    onActionPerformed = onRetryClick,
                )

                is ImageListState.NothingToShow -> MessageView(
                    modifier = Modifier.fillMaxSize(),
                    message = stringResource(id = ImagesR.string.no_images_available)
                )

                is ImageListState.Error -> ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    message = state.message,
                    onRetryClick = onRetryClick
                )

                ImageListState.Ignore -> { /* do nothing */ }
            }
        }
    }
}

@Composable
private fun ContentView(
    state: ImageListState.Content,
    onDropdownClick: (ImageListDropdownMenu) -> Unit,
    onItemClick: (MenuItem) -> Unit,
    onDismissRequest: (ImageListDropdownMenu) -> Unit,
    onClearFilterClick: (ImageListDropdownMenu) -> Unit,
    snackbarHostState: SnackbarHostState,
    onActionPerformed: () -> Unit
) {
    Column {
        FilterView(
            filter = state.filter,
            onDropdownClick = onDropdownClick,
            onItemClick = onItemClick,
            onDismissRequest = onDismissRequest,
            onClearFilterClick = onClearFilterClick,
        )
        Images(images = state.images)
        if (state.snackbarMessage != null) {
            MySnackbar(
                snackbarHostState = snackbarHostState,
                message = state.snackbarMessage,
                actionLabel = stringResource(id = CoreR.string.retry),
                onActionPerformed = onActionPerformed
            )
        }
    }
}

@Composable
private fun FilterView(
    filter: ImageListDropdownMenu,
    onDropdownClick: (ImageListDropdownMenu) -> Unit,
    onItemClick: (MenuItem) -> Unit,
    onDismissRequest: (ImageListDropdownMenu) -> Unit,
    onClearFilterClick: (ImageListDropdownMenu) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        DropdownView(
            modifier = Modifier.weight(3f),
            menu = filter,
            onDropdownClick = { onDropdownClick(filter) },
            onItemClick = onItemClick,
            onDismissRequest = onDismissRequest,
        )
        Spacer(modifier = Modifier.width(Dimens.spacing8))
        MyButton(
            modifier = Modifier.weight(1f),
            title = stringResource(id = CoreR.string.clear),
            onClick = { onClearFilterClick(filter) }
        )
    }
}

@Composable
private fun Images(
    images: List<Image>
) {
    LazyColumn(
        contentPadding = PaddingValues(all = Dimens.spacing8),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacing8)
    ) {
        items(images) { image -> ImageItemView(image = image) }
    }
}

@Composable
private fun ErrorView(
    modifier: Modifier = Modifier,
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(bottom = Dimens.spacing8)
    ) {
        MessageView(
            modifier = Modifier.weight(1f),
            message = message
        )
        MyButton(
            title = stringResource(CoreR.string.retry),
            onClick = onRetryClick
        )
    }
}

