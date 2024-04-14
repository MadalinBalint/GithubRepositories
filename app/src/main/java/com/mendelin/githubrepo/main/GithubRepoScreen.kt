package com.mendelin.githubrepo.main

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.mendelin.githubrepo.domain.model.Repository
import com.mendelin.githubrepo.main.item.ListPhoneLandscapeContent
import com.mendelin.githubrepo.main.item.ListPhonePortraitContent
import com.mendelin.githubrepo.main.item.ListTabletContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubRepoScreen(
    viewModel: GithubRepoViewModel = viewModel(),
    isTablet: Boolean
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    val repositories = viewModel.repositoriesState.collectAsLazyPagingItems()

    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    val searchItems = rememberSaveable(
        saver = listSaver<MutableList<String>, String>(
            save = {
                if (it.isNotEmpty()) it.toList() else listOf()
            },
            restore = {
                it.toMutableStateList()
            }
        )
    ) {
        mutableStateListOf()
    }

    var selectedItem by rememberSaveable { mutableStateOf<Repository?>(null) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                query = text,
                onQueryChange = {
                    text = it
                },
                onSearch = {
                    active = false
                    if (text.isNotBlank() && !searchItems.contains(text)) {
                        searchItems.add(text)
                    }

                    viewModel.fetchRepositories(text)
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(text = "Search repositories") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                },
                trailingIcon = {
                    if (active) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close icon",
                            modifier = Modifier.clickable {
                                if (text.isNotEmpty()) {
                                    text = ""
                                } else {
                                    active = false
                                }
                            }
                        )
                    }
                }
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                searchItems.forEach {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History icon"
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = it)
                    }
                }
            }
        },
        content = { it ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp)
            ) {
                if (text.isNotEmpty() && repositories.itemCount > 0) {
                    val orientation = LocalConfiguration.current.orientation
                    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

                    if (isTablet) {
                        ListTabletContent(
                            list = repositories, selectedItem = selectedItem,
                            isLandscape = isLandscape,
                        ) { repository ->
                            selectedItem = repository
                        }
                    } else {
                        if (isLandscape) {
                            ListPhoneLandscapeContent(list = repositories)
                        } else {
                            ListPhonePortraitContent(list = repositories)
                        }
                    }
                }

                if (uiState.isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }

                uiState.errorMessage?.let {
                    LaunchedEffect(it) {
                        snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                        viewModel.errorShown()
                    }
                }

                uiState.emptyListMessage?.let {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    )
}
