package com.example.images.navigation.image_list

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.theme.Dimens
import com.example.core.ui.theme.LoremPicsumTheme

@Composable
fun LoadingView(
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

@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewLoadingView() {
    LoremPicsumTheme() {
        Surface {
            LoadingView(Modifier.fillMaxWidth())
        }
    }
}