package com.mendelin.githubrepo.main

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class GithubRepoScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<GithubRepoActivity>()

    lateinit var viewModel: GithubRepoViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = ViewModelProvider(composeRule.activity)[GithubRepoViewModel::class.java]
    }

    @Test
    fun searchBar_input_text_then_remove_content_is_empty() {
        with(composeRule) {
            onNodeWithText("Search repositories").assertExists()
            onNodeWithText("Search repositories").performTextInput("kotlin")
            onNodeWithContentDescription("Close icon").performClick()
            onNodeWithText("Search repositories").assertTextContains("")
        }
    }

    // TODO - add more complex tests for the UI flow
}
