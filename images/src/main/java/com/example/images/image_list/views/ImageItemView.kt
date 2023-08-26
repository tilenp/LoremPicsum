package com.example.images.image_list.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.ui.theme.Dimens
import com.example.core.ui.theme.LoremPicsumTheme
import com.example.core.ui.theme.Sizes
import com.example.domain.model.Image
import com.example.core.R as coreR

@Composable
internal fun ImageItemView(
    modifier: Modifier = Modifier,
    image: Image
) {
    Surface(
        shape = RoundedCornerShape(Dimens.spacing8),
        shadowElevation = Dimens.spacing4,
        color = MaterialTheme.colorScheme.inverseSurface
    ) {
        Column(
            modifier = modifier,
        ) {
            ImageView(
                modifier = Modifier
                    .height(Sizes.smallImage)
                    .fillMaxWidth(),
                downloadUrl = image.downloadUrl
            )
            ImageInfo(
                modifier = Modifier
                    .padding(Dimens.spacing8),
                author = image.author
            )
        }
    }
}

@Composable
private fun ImageView(
    modifier: Modifier,
    downloadUrl: String,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(downloadUrl)
            .crossfade(durationMillis = 200)
            .build(),
        placeholder = painterResource(coreR.drawable.image_placeholder),
        error = painterResource(coreR.drawable.broken_image),
        fallback = painterResource(coreR.drawable.broken_image),
        contentDescription = "ArticleImage",
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ImageInfo(
    modifier: Modifier,
    author: String
) {
    Text(
        modifier = modifier,
        text = author,
        style = MaterialTheme.typography.bodyMedium,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.inverseOnSurface
    )
}

@Preview(
    name = "Light Mode",
    showBackground = true,
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun PreviewPhotoItemView() {
    LoremPicsumTheme {
        Surface {
            ImageItemView(
                modifier = Modifier.height(Sizes.smallImage),
                image = Image(
                    id = "",
                    author = "Alejandro Escamilla",
                    downloadUrl = "https://picsum.photos/id/0/5000/3333"
                ),
            )
        }
    }
}
