plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "employcity.exchangerates"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
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
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.navigationCompose)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKtx)
    kapt(Dependencies.roomCompiler)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitConverterGson)
    implementation(Dependencies.okHttp3LoggingInterceptor)
    implementation(Dependencies.hilt)
    implementation(Dependencies.hiltNavigationCompose)
    kapt(Dependencies.hiltCompiler)
    implementation(Dependencies.constraintLayoutCompose)
    implementation(Dependencies.accompanistSwipeRefresh)
}