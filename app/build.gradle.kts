plugins {
    alias(libs.plugins.android.application) // Android application plugin
    alias(libs.plugins.kotlin.android) // Kotlin Android plugin
    alias(libs.plugins.kotlin.compose) // Jetpack Compose support
    kotlin("plugin.serialization") version "1.9.0" // Serialization support
}

android {
    namespace = "project.pdm.chatr"
    compileSdk = 35

    defaultConfig {
        applicationId = "project.pdm.chatr"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)
    implementation(libs.androidx.activity.compose.v182)
    implementation(libs.androidx.material3.v120)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3.v110)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.foundation.android)
    implementation(libs.core.ktx)
    implementation(libs.androidx.junit.ktx)

    // UI Testing for Compose
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)

    // JUnit for testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core.v400)
    testImplementation (libs.robolectric)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(libs.androidx.navigation.testing)

    // Debugging tools
    debugImplementation(libs.ui.tooling)
}
