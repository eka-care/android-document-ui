plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "eka.care.documents.ui"
    compileSdk = 35

    defaultConfig {
        minSdk = 23

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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.eka.records"
                artifactId = "eka-records-ui"
                version = "1.0.0"
            }
        }
    }
    tasks.named("publishReleasePublicationToMavenLocal") {
        dependsOn(tasks.named("bundleReleaseAar"))
    }
}

dependencies {
    api(libs.eka.health.records)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.eka.ui.kit)
    implementation(libs.compose.shimmer)
    implementation(libs.coil.compose)
    implementation(libs.google.gson)
    implementation(libs.mlkit.scanner)
    implementation(libs.bouquet)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.work.runtime.ktx)
    coreLibraryDesugaring(libs.desugar)
}