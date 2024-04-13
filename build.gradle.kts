// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.9.23" apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
}
