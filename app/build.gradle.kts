plugins {
    // Aplica os plugins no módulo do app (sem "apply false")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Aplica o plugin do Google Services – a versão é resolvida pelo settings.gradle.kts
    id("com.google.gms.google-services")

    // Plugin para serialização Kotlin
    kotlin("plugin.serialization") version "1.9.0"
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

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    // Dependências do AndroidX e Core
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.core.ktx)

    // Ciclo de vida e ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Jetpack Compose e UI
    implementation(libs.androidx.activity.compose.v182)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.material3.v120)
    implementation(libs.androidx.material3.v110)
    implementation(libs.material3)
    implementation(libs.androidx.foundation.android)

    // Ícones e Material Extended
    implementation(libs.androidx.material.icons.extended)

    // Navegação com Compose
    implementation(libs.androidx.navigation.compose)

    // Firebase Firestore
    implementation(libs.firebase.firestore.ktx)

    // DataStore (se necessário)
    implementation(libs.androidx.datastore.preferences)

    // Dependências para testes
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core.v400)
    testImplementation(libs.robolectric)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)

    // Ferramentas de Debug para Compose
    debugImplementation(libs.ui.tooling)


    implementation(libs.kotlinx.serialization.json.v150)

}
