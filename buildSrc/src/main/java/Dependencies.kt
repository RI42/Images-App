import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Deps {
    const val STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${BuildPluginsVersion.KOTLIN}"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${BuildPluginsVersion.KOTLIN}"

    const val KOTLINX_SERIALIZATION_JSON = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}"
    const val KOTLINX_COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val KOTLINX_COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"

    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val VECTORDRAWABLE_ANIMATED = "androidx.vectordrawable:vectordrawable-animated:${Versions.VECTORDRAWABLE_ANIMATED}"
    const val MEDIAROUTER = "androidx.mediarouter:mediarouter:${Versions.MEDIAROUTER}"
    const val EXIFINTERFACE = "androidx.exifinterface:exifinterface:${Versions.EXIFINTERFACE}"
    const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINTLAYOUT}"
    const val VIEWPAGER2 = "androidx.viewpager2:viewpager2:${Versions.VIEWPAGER2}"
    const val CARDVIEW = "androidx.cardview:cardview:${Versions.CARDVIEW}"
    const val BROWSER = "androidx.browser:browser:${Versions.BROWSER}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"
    const val SWIPEREFRESHLAYOUT = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPEREFRESHLAYOUT}"

    const val CIRCLEINDICATOR = "me.relex:circleindicator:${Versions.CIRCLEINDICATOR}"

    const val STFALCON = "com.github.stfalcon:stfalcon-imageviewer:${Versions.STFALCON}"

    const val SECURITY_CRYPTO = "androidx.security:security-crypto:${Versions.SECURITY_CRYPTO}"

    const val MAPS_V3 = "com.google.android.libraries.maps:maps:${Versions.MAPS_V3}"
    const val MAPS_UTILS_V3 = "com.google.maps.android:android-maps-utils-v3:${Versions.MAPS_UTILS_V3}"

    // KTX for the Maps SDK for Android V3 BETA Library
    const val MAPS_V3_KTX = "com.google.maps.android:maps-v3-ktx:${Versions.MAPS_KTX}"
    const val MAPS_UTILS_V3_KTX = "com.google.maps.android:maps-utils-v3-ktx:${Versions.MAPS_KTX}"

    // KTX for the Maps SDK for Android library
    const val MAPS_KTX = "com.google.maps.android:maps-ktx:${Versions.MAPS_KTX}"
    const val MAPS_UTILS_KTX = "com.google.maps.android:maps-utils-ktx:${Versions.MAPS_KTX}"

    // play-services
    const val PLAY_SERVICES_AUTH = "com.google.android.gms:play-services-auth:${Versions.PLAY_AUTH}"
    const val PLAY_SERVICES_LOCATION = "com.google.android.gms:play-services-location:${Versions.PLAY_LOCATION}"
    const val PLAY_SERVICES_MAPS = "com.google.android.gms:play-services-maps:${Versions.PLAY_MAPS}"
    const val PLAY_SERVICES_BASE = "com.google.android.gms:play-services-base:${Versions.PLAY_BASE}"
    const val MAPS_UTILS = "com.google.maps.android:android-maps-utils:${Versions.MAPS_UTILS}"
    //reCaptcha
    const val PLAY_SERVICES_SAFETYNET = "com.google.android.gms:play-services-safetynet:${Versions.PLAY_SAFETYNET}"

    const val COROUTINES_PLAY_SERVICES = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.COROUTINES}"


    const val COIL = "io.coil-kt:coil:${Versions.COIL}"
    const val COIL_GIF = "io.coil-kt:coil-gif:${Versions.COIL}"

    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.LOGGING_INTERCEPTOR}"

    const val JAVAX_ANNOTATION_API = "javax.annotation:javax.annotation-api:${Versions.JAVAX_ANNOTATION}"


    const val TEDPERMISSION = "gun0912.ted:tedpermission:${Versions.TEDPERMISSION}"
    const val DEXTER = "com.karumi:dexter:${Versions.DEXTER}"
    const val TOUCH_IMAGE_VIEW = "com.github.MikeOrtiz:TouchImageView:${Versions.TOUCH_IMAGE_VIEW}"
    const val ANDROID_IMAGE_CROPPER = "com.theartofdev.edmodo:android-image-cropper:${Versions.ANDROID_IMAGE_CROPPER}"
    const val APP_INTRO = "com.github.AppIntro:AppIntro:${Versions.APP_INTRO}"

    // Scan QR-code
    const val ZXING_ANDROID_EMBEDDED = "com.journeyapps:zxing-android-embedded:${Versions.ZXING_ANDROID_EMBEDDED}"
    const val ZXING_CORE = "com.google.zxing:core:${Versions.ZXING_CORE}"

    const val COLLECTION_KTX = "androidx.collection:collection-ktx:${Versions.COLLECTION_KTX}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"

    const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${Versions.ACTIVITY_KTX}"
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT_KTX}"
    const val LIFECYCLE_VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_VIEWMODEL_SAVEDSTATE = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.LIFECYCLE}"
    const val LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_COMMON_JAVA8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.LIFECYCLE}"
    const val LIFECYCLE_SERVICE = "androidx.lifecycle:lifecycle-service:${Versions.LIFECYCLE}"
    const val LIFECYCLE_PROCESS = "androidx.lifecycle:lifecycle-process:${Versions.LIFECYCLE}"

    const val SAVEDSTATE = "androidx.savedstate:savedstate-ktx:${Versions.SAVEDSTATE_KTX}"

    const val NAVIGATION_RUNTIME_KTX = "androidx.navigation:navigation-runtime-ktx:${BuildPluginsVersion.NAVIGATION}"
    const val NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:${BuildPluginsVersion.NAVIGATION}"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${BuildPluginsVersion.NAVIGATION}"

    const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}"
    const val HILT_LIFECYCLE_VIEWMODEL = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.ANDROIDX_HILT}"
    const val HILT_NAVIGATION_FRAGMENT = "androidx.hilt:hilt-navigation-fragment:${Versions.ANDROIDX_HILT}"
    const val HILT_ANDROIDX_COMPILER = "androidx.hilt:hilt-compiler:${Versions.ANDROIDX_HILT}"

    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"

    const val EXOPLAYER_CORE = "com.google.android.exoplayer:exoplayer-core:${Versions.EXOPLAYER}"
    const val EXOPLAYER_UI = "com.google.android.exoplayer:exoplayer-ui:${Versions.EXOPLAYER}"
    const val EXTENSION_MEDIASESSION = "com.google.android.exoplayer:extension-media2:${Versions.EXOPLAYER}"
    const val MEDIA2_SESSION = "androidx.media2:media2-session:${Versions.MEDIA2_SESSION}"

    //room
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"

    const val DATASTORE_PREFERENCES = "androidx.datastore:datastore-preferences:${Versions.DATASTORE}"
    const val DATASTORE_CORE = "androidx.datastore:datastore-core:${Versions.DATASTORE}"

    // firebase
    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"

    const val DESUGAR_JDK_LIBS = "com.android.tools:desugar_jdk_libs:1.0.10"
}


object TestDeps {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val MOCKITO_CORE = "org.mockito:mockito-core:${Versions.MOCKITO}"
    const val MOCKITO_INLINE = "org.mockito:mockito-inline:${Versions.MOCKITO}"
    const val MOCKITO_KOTLIN = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.MOCKITO_KOTLIN}"
    const val TEST_CORE = "androidx.test:core-ktx:${Versions.TEST_CORE}"
    const val ARCH_CORE_TESTING = "androidx.arch.core:core-testing:${Versions.ARCH_CORE_TESTING}"
    const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"
    const val ROOM_TESTING = "androidx.room:room-testing:${Versions.ROOM}"
    const val KOTLINX_COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
    const val LEAKCANARY_ANDROID = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAKCANARY}"
}

object AndroidTestDeps {
    const val JUNIT = "androidx.test.ext:junit-ktx:${Versions.EXT_JUNIT}"
    const val RUNNER = "androidx.test:runner:${Versions.TEST_RUNNER}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
    const val FRAGMENT_TESTING = "androidx.fragment:fragment-testing:${Versions.FRAGMENT_KTX}"
}


fun DependencyHandlerScope.commonDependencies() {
    implementation(Deps.STDLIB)
    implementation(Deps.KOTLIN_REFLECT)
    implementation(Deps.KOTLINX_COROUTINES_CORE)
    implementation(Deps.KOTLINX_COROUTINES_ANDROID)

    implementation(Deps.APPCOMPAT)
    implementation(Deps.VECTORDRAWABLE_ANIMATED)
    implementation(Deps.EXIFINTERFACE)
    implementation(Deps.CONSTRAINTLAYOUT)
    implementation(Deps.CARDVIEW)
    implementation(Deps.MATERIAL)

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

    implementation(Deps.COIL)

    implementation(Deps.RETROFIT)
    implementation(Deps.CONVERTER_GSON)
    implementation(Deps.GSON)
    implementation(Deps.KOTLINX_SERIALIZATION_JSON)

    implementation(Deps.HILT_ANDROID)
    kapt(Deps.HILT_COMPILER)
//    implementation(Deps.HILT_LIFECYCLE_VIEWMODEL)
//    kapt(Deps.HILT_ANDROIDX_COMPILER)

    implementation(Deps.TIMBER)

    implementation(platform(Deps.FIREBASE_BOM))
    implementation(Deps.FIREBASE_CRASHLYTICS)
    implementation(Deps.FIREBASE_ANALYTICS)


    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${BuildPluginsVersion.DETEKT}")

    add("coreLibraryDesugaring", Deps.DESUGAR_JDK_LIBS)

    testImplementation(TestDeps.JUNIT)
    testImplementation(TestDeps.TEST_CORE)
    testImplementation(TestDeps.ROBOLECTRIC)
    androidTestImplementation(AndroidTestDeps.JUNIT)
}

private fun DependencyHandler.implementation(depName: Any) {
    add("implementation", depName)
}

private fun DependencyHandler.kapt(depName: Any) {
    add("kapt", depName)
}

private fun DependencyHandler.testImplementation(depName: Any) {
    add("testImplementation", depName)
}

private fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

fun DependencyHandler.detektPlugins(dependencyNotation: Any): Dependency? =
    add("detektPlugins", dependencyNotation)