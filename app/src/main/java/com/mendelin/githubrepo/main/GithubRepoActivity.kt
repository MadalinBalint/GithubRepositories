package com.mendelin.githubrepo.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.mendelin.githubrepo.ui.theme.GithubRepoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GithubRepoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GithubRepoTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    GithubRepoScreen(isTablet = isTablet())
                }
            }
        }
    }

    @Composable
    fun isTablet(): Boolean {
        val configuration = LocalConfiguration.current

        return if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configuration.screenHeightDp > 600
        } else {
            configuration.screenWidthDp > 600
        }
    }
}
