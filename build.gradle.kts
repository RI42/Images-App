import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${BuildPluginsVersion.AGP}")
        classpath(kotlin("gradle-plugin", version = BuildPluginsVersion.KOTLIN))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${BuildPluginsVersion.NAVIGATION}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}")
//        classpath("com.google.gms:google-services:${BuildPluginsVersion.GOOGLE_SERVICES}")
//        classpath("com.google.firebase:firebase-crashlytics-gradle:${BuildPluginsVersion.FIREBASE_CRASHLYTICS_GRADLE}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        gradlePluginPortal()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf(
                "-Xuse-experimental=" +
                        "kotlin.Experimental," +
                        "kotlinx.coroutines.ExperimentalCoroutinesApi," +
                        "kotlinx.coroutines.InternalCoroutinesApi," +
                        "kotlinx.coroutines.FlowPreview"
            )
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
