package com.mendelin.githubrepo.main.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.mendelin.githubrepo.domain.model.Repository
import com.mendelin.githubrepo.util.Constants


@Composable
fun ListTabletContent(
    list: LazyPagingItems<Repository>,
    selectedItem: Repository?,
    isLandscape: Boolean,
    onClick: (Repository) -> Unit
) {
    val cells = if (isLandscape) 4 else 2
    val cellSize = 128.dp
    val listState = rememberLazyGridState()

    Row {
        LazyVerticalGrid(
            columns = GridCells.Fixed(cells),
            modifier = Modifier
                .fillMaxHeight()
                .width(cellSize.times(cells))
                .semantics { testTag = Constants.LIST_TEST_TAG },
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
        ) {
            items(list.itemCount, key = list.itemKey { it.id }) { index ->
                val item = list[index]!!
                OwnerAvatarImage(imageUrl = item.ownerAvatar, size = cellSize) {
                    onClick(list[index]!!)
                }
            }
        }

        selectedItem?.let { item ->
            val padding = if (isLandscape) 64.dp else 32.dp
            Column(
                modifier = Modifier
                    .padding(start = padding, end = padding, bottom = 16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OwnerAvatarImage(imageUrl = item.ownerAvatar, size = 320.dp)
                }

                Spacer(modifier = Modifier.height(32.dp))
                OwnerName(name = item.ownerName)
                RepositoryName(name = item.repositoryName)
                RepositoryTitle(title = item.repositoryTitle)

                Spacer(modifier = Modifier.height(16.dp))

                RepositoryDescription(
                    description = item.repositoryDesc,
                    showHeader = true,
                    isTablet = true
                )

                item.language?.let { language ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Language(language = language)
                }

                if (item.licenseType != null && item.licenseUrl != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LicenseType(licenseType = item.licenseType)
                    LicenseUrl(licenseUrl = item.licenseUrl, isTablet = true)
                }

                item.topics?.let { topics ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Topics(topics = topics)
                }

                Spacer(modifier = Modifier.height(16.dp))

                RepositoryUrl(url = item.repositoryUrl, showHeader = true, isTablet = true)
            }
        }
    }
}
