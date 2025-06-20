import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.0.21"
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
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
            buildConfigField("String", "FCM_BACKEND_URL", "\"${localProperties.getProperty("fcm_backend_url")}\"")
            buildConfigField("String", "SUPABASE_URL", "\"${localProperties.getProperty("supabase_url")}\"")
            buildConfigField("String", "SUPABASE_KEY", "\"${localProperties.getProperty("supabase_anon_key")}\"")
        }
        
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Supabase urls
            buildConfigField("String", "FCM_BACKEND_URL", "\"${localProperties.getProperty("fcm_backend_url")}\"")
            buildConfigField("String", "SUPABASE_URL", "\"${localProperties.getProperty("supabase_url")}\"")
            buildConfigField("String", "SUPABASE_KEY", "\"${localProperties.getProperty("supabase_anon_key")}\"")
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

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
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
    implementation(libs.androidx.room.compiler)
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