plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.example.chatterbox"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chatterbox"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        debug {
            buildConfigField("String", "SUPABASE_URL", "\"https://lhvqciwwwjvtaefhyiey.supabase.co/\"")
            buildConfigField("String", "SUPABASE_KEY", "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxodnFjaXd3d2p2dGFlZmh5aWV5Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1MDAwNTYxOCwiZXhwIjoyMDY1NTgxNjE4fQ.vEtCWe2H4y7genGilERNZLy_PFWw1JF8vmKcnvtis_w\"")
        }
        
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Supabase urls
            buildConfigField("String", "SUPABASE_URL", "\"https://lhvqciwwwjvtaefhyiey.supabase.co/\"")
            buildConfigField("String", "SUPABASE_KEY", "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxodnFjaXd3d2p2dGFlZmh5aWV5Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1MDAwNTYxOCwiZXhwIjoyMDY1NTgxNjE4fQ.vEtCWe2H4y7genGilERNZLy_PFWw1JF8vmKcnvtis_w\"")
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
        buildConfig = true
    }
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
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Extra icons
    implementation(libs.androidx.material.icons.extended.android)

    // Firebase
    // BoM
    implementation(platform(libs.firebase.bom))

    // Firebase (analytics)
    implementation(libs.firebase.analytics)

    // Firestore
    implementation(libs.firebase.firestore)

    // Authentication
    implementation(libs.firebase.auth)

    // Messaging
    implementation(libs.firebase.messaging)

    // Dependencies for the Credential Manager libraries and specify their versions
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Koin (for dependency injection)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Google Auth idk
    implementation(libs.play.services.auth)

    // Navigation
    implementation(libs.navigation.compose)

    // Serialization
    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // okHttp
    implementation(libs.okhttp)

    // coil
    implementation(libs.coil.compose)

}