@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.application")
    id("android.application.compose")
    id("android.hilt")
    alias(libs.plugins.junit5)
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    defaultConfig {
        applicationId = "com.myproject.gymphysique"
        versionCode = getVersionCode()
        versionName = getVersionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = GymPhysiqueBuildType.DEBUG.applicationIdSuffix
        }
        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = GymPhysiqueBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
        }
    }
    namespace = "com.myproject.gymphysique"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":feature:measure"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:charts"))
    implementation(project(":feature:accountSetup"))

    implementation(project(":core:designsystem"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(mapOf("path" to ":baselineprofile")))
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.timber)
    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
}

// androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
configurations.configureEach {
    resolutionStrategy {
        force(libs.junit4)
        // Temporary workaround for https://issuetracker.google.com/174733673
        force("org.objenesis:objenesis:2.6")
    }
}

val checkReleaseVersion by tasks.registering {
    doLast {
        val versionName = android.defaultConfig.versionName
        if (versionName?.matches("\\d+(\\.\\d+)+".toRegex()) == false) {
            throw GradleException(
                "Version name for release builds can only be numeric (like 1.0.0), but was $versionName\n" +
                    "Please use git tag to set version name on the current commit and try again\n" +
                    "For example: git tag -a 1.0.0 -m 'tag message'"
            )
        }
    }
}

tasks.whenTaskAdded {
    if (name.contains("assemble") &&
        name.contains("Release")
    ) {
        dependsOn(checkReleaseVersion)
    }
}
