@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.images.navigation.image_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.ui.theme.Dimens
import com.example.domain.model.Image
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
internal fun ImageListScreen(
    viewModel: ImageListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ImageListScreen(state = state)
}

@Composable
private fun ImageListScreen(
    state: ImageListState
) {
    when (state) {
        is ImageListState.Loading -> LoadingView()
        is ImageListState.Data -> Images(images = state.images)
        is ImageListState.NothingToShow -> {}
        is ImageListState.Error -> {}
    }
}

@Composable
private fun LoadingView(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier
            .wrapContentSize(Alignment.Center)
            .padding(Dimens.spacing16)
            .testTag("LoadingIndicator"),
        color = MaterialTheme.colorScheme.secondary
    )
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

