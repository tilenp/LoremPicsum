package com.example.images.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.core.ui.theme.Dimens
import com.example.images.image_list.model.FilterItem
import com.example.images.image_list.model.ImageListFilter
import com.example.images.R as ImagesR

@Composable
internal fun DropdownFilterView(
    modifier: Modifier,
    filter: ImageListFilter,
    onDropdownClick: () -> Unit,
    onItemClick: (FilterItem) -> Unit,
    onDismissRequest: (ImageListFilter) -> Unit
) {
    when (filter) {
        is ImageListFilter.Author -> AuthorFilterView(
            modifier = modifier,
            filter = filter,
            onDropdownClick = onDropdownClick,
            onItemClick = onItemClick,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun AuthorFilterView(
    modifier: Modifier,
    filter: ImageListFilter.Author,
    onDropdownClick: () -> Unit,
    onItemClick: (FilterItem) -> Unit,
    onDismissRequest: (ImageListFilter) -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onSurface)
                .clickable(onClick = { onDropdownClick() })
                .padding(Dimens.spacing8),
            text = filter.items.firstOrNull { it.isSelected }?.text
                ?: stringResource(id = ImagesR.string.select_author),
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onSurface),
            expanded = filter.expanded,
            onDismissRequest = { onDismissRequest(filter) },
        ) {
            filter.items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = Modifier.padding(Dimens.spacing8),
                            text = item.text,
                            style = MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    },
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}