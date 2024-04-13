package com.mendelin.githubrepo.main.item

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.mendelin.githubrepo.R
import com.mendelin.githubrepo.domain.Resource
import com.mendelin.githubrepo.ui.theme.GithubRepoTheme


@Composable
fun OwnerAvatarImage(imageUrl: String, size: Dp, onClick: (() -> Unit)? = null) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = stringResource(R.string.avatar_description),
        contentScale = ContentScale.Fit,
        placeholder = painterResource(id = R.drawable.placeholder),
        error = painterResource(id = R.drawable.error),
        modifier = Modifier
            .size(size)
            .clickable { onClick?.invoke() },
    )
}

@Composable
fun OwnerName(name: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = stringResource(R.string.person_description)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name)
    }
}

@Composable
fun RepositoryName(name: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = Icons.Default.Topic,
            contentDescription = stringResource(R.string.topic_description)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, textAlign = TextAlign.Start)
    }
}

@Composable
fun RepositoryTitle(title: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = Icons.Default.Topic,
            contentDescription = stringResource(R.string.topic_description)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, textAlign = TextAlign.Start)
    }
}

@Composable
fun RepositoryDescription(description: String, showHeader: Boolean, isTablet: Boolean) {
    Column {
        if (showHeader) {
            Text(text = stringResource(R.string.description), fontWeight = FontWeight.Bold)
        }
        if (isTablet) {
            Text(text = description, style = MaterialTheme.typography.bodyLarge)
        } else
            Text(text = description, style = MaterialTheme.typography.bodyMedium, maxLines = 5)
    }
}

@Composable
fun RepositoryUrl(url: String?, showHeader: Boolean, isTablet: Boolean) {
    if (!url.isNullOrEmpty() || url?.startsWith("http") == true) {
        Column {
            if (showHeader) {
                Text(text = stringResource(R.string.repository_url), fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = stringResource(R.string.link_description)
                )
                Spacer(modifier = Modifier.width(8.dp))
                ClickableUrlContent(url = url, isTablet = isTablet)
            }
        }
    }
}

@Composable
fun Language(language: String) {
    Column {
        Text(text = stringResource(R.string.programming_language), fontWeight = FontWeight.Bold)
        Text(text = language)
    }

}

@Composable
fun LicenseType(licenseType: String) {
    Column {
        Text(text = stringResource(R.string.license_type), fontWeight = FontWeight.Bold)
        Text(text = licenseType)
    }
}

@Composable
fun LicenseUrl(licenseUrl: String?, isTablet: Boolean) {
    if (!licenseUrl.isNullOrEmpty() || licenseUrl?.startsWith("http") == true) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = stringResource(R.string.link_description)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ClickableUrlContent(url = licenseUrl, isTablet = isTablet)
        }
    }
}

@Composable
fun Topics(topics: String) {
    Column {
        Text(text = stringResource(R.string.topics), fontWeight = FontWeight.Bold)
        Text(text = topics)
    }
}

@Composable
fun EndOfList(message: String) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = message)
    }
}

@Composable
private fun ClickableUrlContent(url: String, isTablet: Boolean) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.Blue, fontWeight = FontWeight.Bold
            )
        ) {
            append(url)
        }
        pushStringAnnotation(
            tag = "URL", annotation = url
        )
        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        },
        style = if (isTablet) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
    )
}
/** Previews of the screen items */
@Preview(showBackground = true)
@Composable
fun OwnerAvatarImagePreview() {
    GithubRepoTheme {
        OwnerAvatarImage(imageUrl = "https://avatars.githubusercontent.com/u/7304399?v=4", size = 128.dp, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun OwnerNamePreview() {
    GithubRepoTheme {
        OwnerName(name = "Steve Jobs")
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryNamePreview() {
    GithubRepoTheme {
        RepositoryName(name = "macOS")
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryTitlePreview() {
    GithubRepoTheme {
        RepositoryTitle(title = "macOS/Steve Jobs")
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryDescriptionPhonePreview() {
    GithubRepoTheme {
        RepositoryDescription(
            description = "macOS (previously known as OS X) is the operating system developed by Apple Inc. for its Mac line of personal computers and workstations. The abbreviation \"macOS\" stands for \"Macintosh Operating System. It was first introduced in 2001 as the successor to the classic Mac OS. Since then, it has undergone many updates and improvements to become the sophisticated and user-friendly operating system it is today.",
            showHeader = true,
            isTablet = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryDescriptionTabletPreview() {
    GithubRepoTheme {
        RepositoryDescription(
            description = "macOS (previously known as OS X) is the operating system developed by Apple Inc. for its Mac line of personal computers and workstations. The abbreviation \"macOS\" stands for \"Macintosh Operating System. It was first introduced in 2001 as the successor to the classic Mac OS. Since then, it has undergone many updates and improvements to become the sophisticated and user-friendly operating system it is today.",
            showHeader = true,
            isTablet = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryUrlPhonePreview() {
    GithubRepoTheme {
        RepositoryUrl(
            url = "https://github.com/JetBrains",
            showHeader = true,
            isTablet = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryUrlTabletPreview() {
    GithubRepoTheme {
        RepositoryUrl(
            url = "https://github.com/JetBrains",
            showHeader = true,
            isTablet = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LanguagePreview() {
    GithubRepoTheme {
        Language(
            language = "Kotlin",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LicenseTypePreview() {
    GithubRepoTheme {
        LicenseType(
            licenseType = "MIT License",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LicenseUrlPhonePreview() {
    GithubRepoTheme {
        LicenseUrl(
            licenseUrl = "https://api.github.com/licenses/mit",
            isTablet = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LicenseUrlTabletPreview() {
    GithubRepoTheme {
        LicenseUrl(
            licenseUrl = "https://api.github.com/licenses/mit",
            isTablet = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopicsPreview() {
    GithubRepoTheme {
        Topics(
            topics = "kotlin, kotlin-android",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EndOfListPreview() {
    GithubRepoTheme {
        EndOfList(
            message = "Only the first 1000 search results are available\nhttps://docs.github.com/v3/search/"
        )
    }
}
