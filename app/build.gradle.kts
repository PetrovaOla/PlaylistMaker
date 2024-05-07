@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "petrova.ola.playlistmaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "petrova.ola.playlistmaker"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.activity)
    annotationProcessor(libs.compiler)

//    работа с сетью
    implementation (libs.gson)
    implementation (libs.squareup.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.squareup.retrofit)
    implementation(libs.glide)

//    test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

//
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

//    di
    implementation(libs.koin.android)

    //delegate adapter
    implementation(libs.adapterdelegates4.kotlin.dsl)
    implementation(libs.adapterdelegates4.kotlin.dsl.viewbinding)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

//   корутины
    implementation(libs.kotlinx.coroutines.android)

//    room
    implementation(libs.androidx.room.runtime) // библиотека Room
    kapt(libs.androidx.room.compiler) // Kotlin-кодогенератор

//permission
    implementation (libs.com.markodevcic.peko)

    implementation(libs.androidx.room.ktx) // поддержка корутин
}