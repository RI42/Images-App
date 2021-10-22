plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version BuildPluginsVersion.KOTLIN
//    id("com.google.gms.google-services")
//    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = SdkVersion.COMPILE_SDK_VERSION
    buildToolsVersion = SdkVersion.BUILD_TOOLS_VERSION

    defaultConfig {
        minSdk = SdkVersion.MIN_SDK_VERSION
        targetSdk = SdkVersion.TARGET_SDK_VERSION

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
        resourceConfigurations.add("en")
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    buildFeatures {
        viewBinding = true
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

    implementation(Deps.STDLIB)
    implementation(Deps.KOTLIN_REFLECT)
    implementation(Deps.KOTLINX_SERIALIZATION_JSON)
    implementation(Deps.KOTLINX_COROUTINES_CORE)
    implementation(Deps.KOTLINX_COROUTINES_ANDROID)

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
    implementation("jp.wasabeef:recyclerview-animators:4.0.2")
    implementation(Deps.PAGING)
    implementation(Deps.GLIDE)

    implementation("dev.chrisbanes.insetter:insetter:${Versions.INSETTER}")

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

    implementation(Deps.TIMBER)

    implementation(Deps.SWIPEREFRESHLAYOUT)

    implementation(Deps.RETROFIT)
    implementation(Deps.KOTLINX_SERIALIZATION_CONVERTER)
    implementation(Deps.LOGGING_INTERCEPTOR)

    implementation(Deps.ROOM_KTX)
    implementation(Deps.ROOM_RUNTIME)
    kapt(Deps.ROOM_COMPILER)

    coreLibraryDesugaring(Deps.DESUGAR_JDK_LIBS)

    testImplementation(TestDeps.JUNIT)
    testImplementation(TestDeps.ARCH_CORE_TESTING)
    testImplementation(TestDeps.KOTLINX_COROUTINES_TEST)
    testImplementation(TestDeps.MOCKITO_CORE)
    testImplementation(TestDeps.MOCKITO_INLINE)
    testImplementation(TestDeps.MOCKITO_KOTLIN)
    testImplementation(TestDeps.ROBOLECTRIC)
    testImplementation(TestDeps.TEST_CORE)
    testImplementation(TestDeps.ROOM_TESTING)
    testImplementation(AndroidTestDeps.JUNIT)

    testImplementation("com.google.truth:truth:${Versions.TRUTH_GOOGLE}")

    androidTestImplementation("com.google.truth:truth:${Versions.TRUTH_GOOGLE}")

    androidTestImplementation(AndroidTestDeps.FRAGMENT_TESTING)
    androidTestImplementation(AndroidTestDeps.ESPRESSO_CORE)
    androidTestImplementation(AndroidTestDeps.RUNNER)

    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:${Versions.HILT}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Versions.HILT}")

    // For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:${Versions.HILT}")
    kaptTest("com.google.dagger:hilt-android-compiler:${Versions.HILT}")
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableTransformForLocalTests = true
}