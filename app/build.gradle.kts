import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.example.nearbyapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.nearbyapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.preference:preference-ktx:1.2.1")
    val navVersion = "2.5.3"
    val lifecycleVersion = "2.6.2"
    val fragmentVersion = "1.6.1"
    val retrofit = "2.9.0"
    val converter = "2.9.0"
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // live data and  view model
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // image downloading library
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    // fragments
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")

    // retrofit and gson library
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$converter")
    implementation("com.google.code.gson:gson:2.10.1")

    // kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // room components
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // hilt for dependency injection
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")

    // palette
    implementation("androidx.palette:palette-ktx:1.0.0")

    // lorem text
    implementation("com.thedeanda:lorem:2.2")

    // splash screen api
    implementation("androidx.core:core-splashscreen:1.0.1")
}

kapt {
    correctErrorTypes = true
}
