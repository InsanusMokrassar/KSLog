buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath (libs.android.tools.build)
        classpath (libs.android.dexcount)
        classpath (libs.kotlin.gradle.plugin)
        classpath (libs.kotlin.dokka.plugin)
        classpath (libs.github.release)
    }
}
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.dokka)
    alias(libs.plugins.versions)
}

apply(plugin = "com.android.library")

project.version = "$version"
project.group = "$group"

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    js(IR) {
        browser()
        nodejs()
    }
    androidTarget {
        publishAllLibraryVariants()
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    linuxArm64()
    linuxArm32Hfp()
    linuxX64()
    wasm {
        browser()
        nodejs()
        d8()
    }
    wasm32()
    mingwX64()
    mingwX86()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin)
            }
        }
        commonTest {
            dependencies {
                implementation (libs.kotlin.test.common)
                implementation (libs.kotlin.test.annotations.common)
                implementation (libs.kotlin.test.junit)
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

apply (from = "defaultAndroidSettings.gradle")
apply (from = "github_release.gradle")
apply (from = "publish.gradle")
apply (from = "dokka.gradle")
