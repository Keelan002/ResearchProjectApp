plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

android {
    namespace = "mtu.research_project.researchprojectapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "mtu.research_project.researchprojectapp"
        minSdk = 24
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core libraries
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.6.2@aar>")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose libraries
    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    testImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Camera and Lifecycle libraries
    implementation("androidx.camera:camera-core:1.2.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Material Icons Extended
    implementation ("androidx.compose.material:material-icons-extended")

    val navVersion = "2.7.3"
    implementation ("androidx.navigation:navigation-compose:$navVersion")

    val lifecycle_version = "2.6.2"
    val camerax_version = "1.3.0-rc01"
    val accompanist_version = "0.32.0"
    val koin_version = "3.4.2"
    val koin_ksp_version = "1.2.2"

    //camera dependancies

    implementation ("androidx.camera:camera-camera2:$camerax_version")
    implementation ("androidx.camera:camera-lifecycle:$camerax_version")
    implementation ("androidx.camera:camera-view:$camerax_version")
    implementation ("androidx.camera:camera-extensions:$camerax_version")



    //// ACCOMPANIST ////
    implementation ("com.google.accompanist:accompanist-permissions:$accompanist_version")



    //LIFE CYCLE
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")



    ////KOIN BASE////
    implementation ("io.insert-koin:koin-core:$koin_version")
    implementation ("io.insert-koin:koin-android:$koin_version")
    implementation ("io.insert-koin:koin-androidx-compose:3.4.6")



    ////KOIN KSP////
    implementation ("io.insert-koin:koin-annotations:$koin_ksp_version")
    ksp ("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    //androidTestImplementation platform("androidx.compose:compose-bom:2022.10.00")
    //implementation platform("androidx.compose:compose-bom:1.0.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4")
    debugImplementation ("androidx.compose.ui:ui-tooling")
    debugImplementation ("androidx.compose.ui:ui-test-manifest")
}