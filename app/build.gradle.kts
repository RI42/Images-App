plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version BuildPluginsVersion.KOTLIN
//    id("org.jetbrains.kotlin.plugin.serialization") version BuildPluginsVersion.KOTLIN
//    id("com.google.gms.google-services")
//    id("com.google.firebase.crashlytics")
}

android {
    compileSdkVersion(SdkVersion.COMPILE_SDK_VERSION)
    buildToolsVersion(SdkVersion.BUILD_TOOLS_VERSION)

    defaultConfig {
        minSdkVersion(SdkVersion.MIN_SDK_VERSION)
        targetSdkVersion(SdkVersion.TARGET_SDK_VERSION)

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
        resConfigs("en", "ru")
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = isMinifyEnabled
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            isMinifyEnabled = false
            isShrinkResources = isMinifyEnabled
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation("androidx.paging:paging-runtime-ktx:3.0.0-alpha13")
    implementation("com.yuyakaido.android:card-stack-view:2.3.4")

    implementation(Deps.STDLIB)
    implementation(Deps.KOTLIN_REFLECT)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation(Deps.KOTLINX_COROUTINES_CORE)
    implementation(Deps.KOTLINX_COROUTINES_ANDROID)

    implementation(Deps.COIL)
    implementation(Deps.COIL_GIF)

    implementation(Deps.APPCOMPAT)
    implementation(Deps.VECTORDRAWABLE_ANIMATED)
    implementation(Deps.MEDIAROUTER)
    implementation(Deps.EXIFINTERFACE)
    implementation(Deps.CONSTRAINTLAYOUT)
    implementation(Deps.VIEWPAGER2)
    implementation(Deps.CARDVIEW)
    implementation(Deps.BROWSER)
    implementation(Deps.MATERIAL)
    implementation(Deps.RECYCLERVIEW)
    implementation ("androidx.paging:paging-runtime:3.0.0-alpha13")

    implementation("dev.chrisbanes.insetter:insetter:0.4.0")
    implementation("dev.chrisbanes.insetter:insetter-dbx:0.4.0")

    implementation(Deps.COLLECTION_KTX)
    implementation(Deps.CORE_KTX)

    implementation(Deps.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Deps.LIFECYCLE_VIEWMODEL_SAVEDSTATE)
    implementation(Deps.LIFECYCLE_RUNTIME_KTX)
    implementation(Deps.LIFECYCLE_LIVEDATA_KTX)
    implementation(Deps.LIFECYCLE_COMMON_JAVA8)

    implementation(Deps.SAVEDSTATE)

    implementation(Deps.ACTIVITY_KTX)
    implementation(Deps.FRAGMENT_KTX)

    implementation(Deps.NAVIGATION_RUNTIME_KTX)
    implementation(Deps.NAVIGATION_FRAGMENT_KTX)
    implementation(Deps.NAVIGATION_UI_KTX)

    implementation(Deps.HILT_ANDROID)
    kapt(Deps.HILT_COMPILER)
    implementation(Deps.HILT_LIFECYCLE_VIEWMODEL)
    implementation(Deps.HILT_NAVIGATION_FRAGMENT)
    kapt(Deps.HILT_ANDROIDX_COMPILER)

    implementation(Deps.TIMBER)

    implementation(Deps.SWIPEREFRESHLAYOUT)

    implementation(Deps.RETROFIT)
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation(Deps.KOTLINX_SERIALIZATION_JSON)
    implementation(Deps.LOGGING_INTERCEPTOR)

//    implementation(Deps.EXOPLAYER)
//    implementation(Deps.EXTENSION_MEDIASESSION)

//    implementation(Deps.PLAY_SERVICES_BASE)
//    implementation(Deps.PLAY_SERVICES_AUTH)
//    implementation(Deps.PLAY_SERVICES_LOCATION)
//    implementation(Deps.PLAY_SERVICES_MAPS)
//    implementation(Deps.MAPS_UTILS)
//    implementation(Deps.MAPS_KTX)
//    implementation(Deps.MAPS_UTILS_KTX)
//    implementation(Deps.COROUTINES_PLAY_SERVICES)

    implementation(Deps.ROOM_KTX)
    implementation(Deps.ROOM_RUNTIME)
    kapt(Deps.ROOM_COMPILER)

    coreLibraryDesugaring(Deps.DESUGAR_JDK_LIBS)
    testImplementation(TestDeps.JUNIT)
    androidTestImplementation(AndroidTestDeps.JUNIT)

//    implementation(platform(Deps.FIREBASE_BOM))
//    implementation(Deps.FIREBASE_CRASHLYTICS)
//    implementation(Deps.FIREBASE_ANALYTICS)
}
