plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
//    id("org.jetbrains.kotlin.kapt") version "1.9.0"
//    id("com.google.devtools.ksp")
}

android {

    signingConfigs {
        create("realease") {
            storeFile = file("D:\\Android\\apk\\generate-apk.jks")
            storePassword = "chaoshen666666"
            keyAlias = "generate-apk"
            keyPassword = "chaoshen666666"
        }
    }
    namespace = "pers.optsimauth.todolist"
    compileSdk = 34

    defaultConfig {
        applicationId = "pers.optsimauth.todolist"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("realease")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
        }
    }


}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)  // 确保使用的 UI 版本一致
    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.places)  // 确保这个库是否需要
    implementation(libs.litert)  // 确保这个库是否需要
    implementation("androidx.compose.material:material-icons-extended")  // 注意这个是否被使用
    implementation("com.google.android.material:material:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    implementation("com.kizitonwose.calendar:compose:2.6.0")
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")

    // Room Library
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.tools.core)
    kapt("androidx.room:room-compiler:2.6.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")

    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("de.raphaelebner:roomdatabasebackup:1.0.1")
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc10")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.core:core-splashscreen:1.0.1")
//    implementation("com.google.code.gson:gson:2.8.9")
//    implementation("com.google.code.gson:gson-extras:2.8.9")
}
