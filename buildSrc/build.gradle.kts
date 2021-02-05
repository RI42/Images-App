plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
    mavenCentral()
}
//
//// константы недоступны на момент выполнения скрипта, придётся менять версию в двух местах, тут и в Versions
//dependencies {
//    compileOnly(gradleApi())
//
//    implementation("com.android.tools.build:gradle:4.1.2")
//    implementation(kotlin("gradle-plugin", version = "1.4.21"))
//    implementation(kotlin("android-extensions"))
//    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.2")
//    implementation("com.google.dagger:hilt-android-gradle-plugin:2.31-alpha")
//    implementation("com.google.gms:google-services:4.3.4")
//    implementation("com.google.firebase:firebase-crashlytics-gradle:2.4.1")
//}
