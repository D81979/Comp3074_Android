plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "ca.gbc.comp3074.padsala.comp3074_roomdatabase_lab"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Room saves and reads customer records.
    implementation("androidx.room:room-runtime:2.8.5")

    // Room helpers for Kotlin coroutines and Flow.
    implementation("androidx.room:room-ktx:2.8.5")

    // KSP generates Room database code.
    ksp("androidx.room:room-compiler:2.8.5")

    // Lets Compose use our ViewModel.
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")

    // Lets Compose observe Flow safely.
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.11.0")
}