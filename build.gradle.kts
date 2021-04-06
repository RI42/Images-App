import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.ben-manes.versions") version BuildPluginsVersion.VERSIONS_PLUGIN
}

buildscript {
    repositories {
        google()
        mavenCentral()
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
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            useIR = true
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-Xuse-experimental=" +
                        "kotlin.Experimental," +
                        "kotlinx.coroutines.ExperimentalCoroutinesApi," +
                        "kotlinx.coroutines.InternalCoroutinesApi," +
                        "kotlinx.coroutines.FlowPreview",
                "-Xallow-result-return-type"
            )
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

fun isNonStable(version: String) = "^[0-9,.v-]+(-r)?$".toRegex().matches(version).not()

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
    checkForGradleUpdate = true
}
