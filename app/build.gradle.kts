plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-kapt")
   // id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)

    kotlin("plugin.serialization") version "2.0.0"

}

android {
    namespace = "com.example.mychatapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mychatapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    configurations {
        "implementation" {
            exclude("org.jetbrains.compose.material", "material-desktop")
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        android.buildFeatures.buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "35.0.0"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation (libs.google.firebase.auth)
    implementation (libs.firebase.bom)
    implementation (libs.firebase.ui.auth)


    // Navigation
    val nav_version = "2.8.3"
    api("androidx.navigation:navigation-fragment-ktx:$nav_version")



   // PreferencesDataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    //GoogleSignIn
    implementation ("com.google.android.gms:play-services-auth:20.5.0")

    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // coil Image
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)


    // google gson
    implementation(libs.gson)

    // DroidLibs base dependencies add
    implementation(libs.base)
    implementation(libs.compose.android)
    implementation("com.github.The-Streamliners.DroidLibs:compose:1.2.14")
    implementation("com.github.The-Streamliners.DroidLibs:utils:1.2.14")
    implementation("com.github.The-Streamliners.DroidLibs:pickers:1.2.18")

    // Koin for Android
    implementation(libs.koin.android)
    implementation(libs.koin.android.v353)
    implementation(libs.koin.android)

    implementation ("io.insert-koin:koin-androidx-compose:3.4.0" ) // or latest version
    //implementation( "io.insert-koin:koin-core:3.4.0")



}