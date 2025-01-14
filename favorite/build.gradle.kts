plugins {
    id("com.android.dynamic-feature")
    id("kotlin-android")
    id("com.google.devtools.ksp") version "1.9.21-1.0.16" // Updated KSP version
}

android {
    namespace = "com.zackone.readnews.favorite"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable = true
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // Core
    implementation(project(":app"))
    implementation(project(":core"))

    // Glide (khusus jika hanya dipakai di modul ini)
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Navigation (hanya jika dipakai langsung di favorite)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}