object Libs {
    object Android {
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
    }

    object Kotlin {
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Activity {
        const val activityCompose = "androidx.activity:activity-compose:${Versions.composeActivity}"
    }

    object ConstraintLayout {
        const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:${Versions.composeConstraint}"
    }

    object Compose {
        const val runtime = "androidx.compose.runtime:runtime:${Versions.compose}"
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiUtil = "androidx.compose.ui:ui-util:${Versions.compose}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val foundation = "androidx.compose.foundation:foundation:${Versions.compose}"
        const val layout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
        const val material = "androidx.compose.material:material:${Versions.compose}"
        const val icons = "androidx.compose.material:material-icons-core:${Versions.compose}"
        const val iconsExtended =
            "androidx.compose.material:material-icons-extended:${Versions.compose}"
        const val composeUiTest = "androidx.compose.ui:ui-test:${Versions.compose}"
        const val composeLifecycle = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
        const val paging = "androidx.paging:paging-compose:${Versions.composePaging}"
        const val animation = "androidx.compose.animation:animation:${Versions.compose}"
    }

    object Navigation {
        const val compose = "androidx.navigation:navigation-compose:${Versions.navigationCompose}"
    }

    object Accompanist {
        private const val version = "0.5.1"
        const val coil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
        const val insets = "dev.chrisbanes.accompanist:accompanist-insets:$version"
    }
}

object ClassPaths {
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Plugins {
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "android"
    const val kotlinKapt = "kapt"
}

object Configs {
    const val applicationId = "com.ericktijerou.jetpuppy"
    const val buildToolsVersion = "30.0.3"
    const val compileSdkVersion = 30
    const val minSdkVersion = 23
    const val targetSdkVersion = 30
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Versions {
    internal const val appcompat = "1.2.0"
    internal const val gradle = "7.0.0-alpha08"
    internal const val coroutines = "1.4.2"
    internal const val materialDesign = "1.3.0"
    internal const val coreKtx = "1.5.0-beta01"
    internal const val composePaging = "1.0.0-alpha07"
    internal const val composeActivity = "1.3.0-alpha02"
    internal const val composeConstraint = "1.0.0-alpha02"
    internal const val navigationCompose = "1.0.0-alpha07"
    const val kotlin = "1.4.30"
    const val compose = "1.0.0-beta01"
}
