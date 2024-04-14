package com.mendelin.githubrepo.main.item

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.mendelin.githubrepo.domain.model.Repository
import com.mendelin.githubrepo.util.Constants

/** Phone in portrait mode */
@Composable
fun ItemPhonePortraitContent(item: Repository) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OwnerAvatarImage(imageUrl = item.ownerAvatar, size = 128.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                OwnerName(name = item.ownerName)
                RepositoryName(name = item.repositoryName)
                RepositoryTitle(title = item.repositoryTitle)
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            if (item.repositoryDesc.isNotBlank()) {
                RepositoryDescription(
                    description = item.repositoryDesc, showHeader = true, isTablet = false
                )
            }
            item.language?.let { language ->
                Spacer(modifier = Modifier.height(8.dp))
                Language(language = language)
            }
            item.topics?.let { topics ->
                Spacer(modifier = Modifier.height(8.dp))
                Topics(topics = topics)
            }
            Spacer(modifier = Modifier.height(8.dp))
            RepositoryUrl(url = item.repositoryUrl, showHeader = false, isTablet = false)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListPhonePortraitContent(list: LazyPagingItems<Repository>) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { testTag = Constants.LIST_TEST_TAG },
        state = listState
    ) {
        items(list.itemCount, key = list.itemKey { it.id }) { index ->
            Row(Modifier.animateItemPlacement()) {
                ItemPhonePortraitContent(item = list[index]!!)
            }
        }
    }
}

/** Phone in landscape mode */
@Composable
fun ItemPhoneLandscapeContent(item: Repository) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxHeight()
            .width(360.dp)
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OwnerAvatarImage(imageUrl = item.ownerAvatar, size = 96.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                OwnerName(name = item.ownerName)
                RepositoryName(name = item.repositoryName)
                RepositoryTitle(title = item.repositoryTitle)
            }
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            if (item.repositoryDesc.isNotBlank()) {
                RepositoryDescription(
                    description = item.repositoryDesc, showHeader = false, isTablet = false
                )
            }
            item.language?.let { language ->
                Spacer(modifier = Modifier.height(8.dp))
                Language(language = language)
            }
            item.topics?.let { topics ->
                Spacer(modifier = Modifier.height(8.dp))
                Topics(topics = topics)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(
                modifier = Modifier.weight(1f, true)
            )
            RepositoryUrl(url = item.repositoryUrl, showHeader = true, isTablet = false)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListPhoneLandscapeContent(list: LazyPagingItems<Repository>) {
    val listState = rememberLazyListState()
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { testTag = Constants.LIST_TEST_TAG },
        state = listState
    ) {
        items(list.itemCount, key = list.itemKey { it.id }) { index ->
            Row(Modifier.animateItemPlacement()) {
                ItemPhoneLandscapeContent(item = list[index]!!)
            }
        }
    }
}
