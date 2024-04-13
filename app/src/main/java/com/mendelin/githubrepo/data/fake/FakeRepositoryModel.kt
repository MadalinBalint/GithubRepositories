package com.mendelin.githubrepo.data.fake

import android.annotation.SuppressLint
import com.mendelin.githubrepo.data.model.LicenseModel
import com.mendelin.githubrepo.data.model.OwnerModel
import com.mendelin.githubrepo.data.model.PermissionsModel
import com.mendelin.githubrepo.data.model.RepositoryModel
import com.mendelin.githubrepo.util.Constants
import java.text.SimpleDateFormat

object FakeRepositoryModel {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)

    fun getRepositoryModel(id: Int) = RepositoryModel(
        id = id,
        node_id = "MDEwOlJlcG9zaXRvcnkzNDMyMjY2",
        name = "kotlin$id",
        full_name = "JetBrains$id/kotlin$id",
        private = false,
        owner = getOwnerModel(id),
        html_url = "https://github.com/JetBrains/kotlin",
        description = "The Kotlin Programming Language.",
        fork = false,
        url = "https://api.github.com/repos/JetBrains/kotlin",
        forks_url = "https://api.github.com/repos/JetBrains/kotlin/forks",
        keys_url = "https://api.github.com/repos/JetBrains/kotlin/keys{/key_id}",
        collaborators_url = "https://api.github.com/repos/JetBrains/kotlin/collaborators{/collaborator}",
        teams_url = "https://api.github.com/repos/JetBrains/kotlin/teams",
        hooks_url = "https://api.github.com/repos/JetBrains/kotlin/hooks",
        issue_events_url = "https://api.github.com/repos/JetBrains/kotlin/issues/events{/number}",
        events_url = "https://api.github.com/repos/JetBrains/kotlin/events",
        assignees_url = "https://api.github.com/repos/JetBrains/kotlin/assignees{/user}",
        branches_url = "https://api.github.com/repos/JetBrains/kotlin/branches{/branch}",
        tags_url = "https://api.github.com/repos/JetBrains/kotlin/tags",
        blobs_url = "https://api.github.com/repos/JetBrains/kotlin/git/blobs{/sha}",
        git_tags_url = "https://api.github.com/repos/JetBrains/kotlin/git/tags{/sha}",
        git_refs_url = "https://api.github.com/repos/JetBrains/kotlin/git/refs{/sha}",
        trees_url = "https://api.github.com/repos/JetBrains/kotlin/git/trees{/sha}",
        statuses_url = "https://api.github.com/repos/JetBrains/kotlin/statuses/{sha}",
        languages_url = "https://api.github.com/repos/JetBrains/kotlin/languages",
        stargazers_url = "https://api.github.com/repos/JetBrains/kotlin/stargazers",
        contributors_url = "https://api.github.com/repos/JetBrains/kotlin/contributors",
        subscribers_url = "https://api.github.com/repos/JetBrains/kotlin/subscribers",
        subscription_url = "https://api.github.com/repos/JetBrains/kotlin/subscription",
        commits_url = "https://api.github.com/repos/JetBrains/kotlin/commits{/sha}",
        git_commits_url = "https://api.github.com/repos/JetBrains/kotlin/git/commits{/sha}",
        comments_url = "https://api.github.com/repos/JetBrains/kotlin/comments{/number}",
        issue_comment_url = "https://api.github.com/repos/JetBrains/kotlin/issues/comments{/number}",
        contents_url = "https://api.github.com/repos/JetBrains/kotlin/contents/{+path}",
        compare_url = "https://api.github.com/repos/JetBrains/kotlin/compare/{base}...{head}",
        merges_url = "https://api.github.com/repos/JetBrains/kotlin/merges",
        archive_url = "https://api.github.com/repos/JetBrains/kotlin/{archive_format}{/ref}",
        downloads_url = "https://api.github.com/repos/JetBrains/kotlin/downloads",
        issues_url = "https://api.github.com/repos/JetBrains/kotlin/issues{/number}",
        pulls_url = "https://api.github.com/repos/JetBrains/kotlin/pulls{/number}",
        milestones_url = "https://api.github.com/repos/JetBrains/kotlin/milestones{/number}",
        notifications_url = "https://api.github.com/repos/JetBrains/kotlin/notifications{?since,all,participating}",
        labels_url = "https://api.github.com/repos/JetBrains/kotlin/labels{/name}",
        releases_url = "https://api.github.com/repos/JetBrains/kotlin/releases{/id}",
        deployments_url = "https://api.github.com/repos/JetBrains/kotlin/deployments",
        created_at = dateFormat.parse("2012-02-13T17:29:58Z")!!,
        updated_at = dateFormat.parse("2024-04-10T02:55:07Z")!!,
        pushed_at = dateFormat.parse("2024-04-10T00:36:58Z")!!,
        git_url = "git://github.com/JetBrains/kotlin.git",
        ssh_url = "git@github.com:JetBrains/kotlin.git",
        clone_url = "https://github.com/JetBrains/kotlin.git",
        svn_url = "https://github.com/JetBrains/kotlin",
        homepage = "https://kotlinlang.org",
        size = 2731682,
        stargazers_count = 47373,
        watchers_count = 47373,
        language = "Kotlin",
        has_issues = false,
        has_projects = false,
        has_downloads = true,
        has_wiki = false,
        has_pages = false,
        has_discussions = false,
        forks_count = 5603,
        mirror_url = null,
        archived = false,
        disabled = false,
        open_issues_count = 179,
        license = getLicenseModel(),
        allow_forking = true,
        is_template = false,
        web_commit_signoff_required = false,
        topics = listOf(
            "compiler",
            "gradle-plugin",
            "intellij-plugin",
            "kotlin",
            "kotlin-library",
            "maven-plugin",
            "programming-language",
            "wasm",
            "webassembly"
        ),
        visibility = "public",
        forks = 5603,
        open_issues = 179,
        watchers = 47373,
        default_branch = "master",
        permissions = getPermissionsModel(),
        score = 1.0f,
    )

    fun getOwnerModel(id: Int) =
        OwnerModel(
            login = "JetBrains$id",
            id = id,
            node_id = "MDEyOk9yZ2FuaXphdGlvbjg3ODQzNw==",
            avatar_url = "https://avatars.githubusercontent.com/u/878437?v=4",
            gravatar_id = "",
            url = "https://api.github.com/users/JetBrains",
            html_url = "https://github.com/JetBrains",
            followers_url = "https://api.github.com/users/JetBrains/followers",
            following_url = "https://api.github.com/users/JetBrains/following{/other_user}",
            gists_url = "https://api.github.com/users/JetBrains/gists{/gist_id}",
            starred_url = "https://api.github.com/users/JetBrains/starred{/owner}{/repo}",
            subscriptions_url = "https://api.github.com/users/JetBrains/subscriptions",
            organizations_url = "https://api.github.com/users/JetBrains/orgs",
            repos_url = "https://api.github.com/users/JetBrains/repos",
            events_url = "https://api.github.com/users/JetBrains/events{/privacy}",
            received_events_url = "https://api.github.com/users/JetBrains/received_events",
            type = "Organization",
            site_admin = false
        )

    fun getLicenseModel() =
        LicenseModel(
            key = "mit",
            name = "MIT License",
            spdx_id = "MIT",
            url = "https://api.github.com/licenses/mit",
            node_id = "MDc6TGljZW5zZTEz"
        )

    fun getPermissionsModel() =
        PermissionsModel(
            admin = false,
            maintain = false,
            push = false,
            triage = false,
            pull = true
        )
}
