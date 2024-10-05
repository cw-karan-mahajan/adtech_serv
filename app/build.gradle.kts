plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
}

android {

    namespace = "tv.cloudwalker.cwnxt.launcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "tv.cloudwalker.cwnxt.launcher"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    /*signingConfigs {
        create("development") {
            keyAlias = "devel"
            keyPassword = "cloudwalker"
            storeFile = file("cloudwalker.jks")
            storePassword = "2256081445"
        }
        create("production") {
            keyAlias = "production"
            keyPassword = "cloudwalker"
            storeFile = file("cloudwalker.jks")
            storePassword = "2256081445"
        }
    }*/

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "SERVER_URL", "\"${project.properties["server_url"]}\"")
            buildConfigField("String", "UTILITY_SERVER_URL", "\"${project.properties["utility_server_url"]}\"")
            buildConfigField("String", "ANALYTICS_SERVER_URL", "\"${project.properties["analytics_server_url"]}\"")
            buildConfigField("String", "SERVICE_SERVER_URL", "\"${project.properties["service_server_url"]}\"")
            buildConfigField("String", "FLURRY_API_KEY", "\"${project.properties["flurry_api_key"]}\"")
            buildConfigField("String", "NATS_SERVER_1", "\"${project.properties["nats_server_1"]}\"")
            buildConfigField("String", "NATS_SERVER_2", "\"${project.properties["nats_server_2"]}\"")
            buildConfigField("String", "NATS_SERVER_3", "\"${project.properties["nats_server_3"]}\"")
            buildConfigField("String", "NATS_ID", "\"${project.properties["nats_id"]}\"")
            buildConfigField("String", "NATS_PASS", "\"${project.properties["nats_pass"]}\"")
            buildConfigField("String", "POSTHOG_API_KEY", "\"${project.properties["posthog_api_key"]}\"")
            buildConfigField("String", "POSTHOG_HOST", "\"${project.properties["posthog_host"]}\"")
            buildConfigField("String", "PostHogProjectName", "\"Launcher_Generic_Carousel_\"")
            isDebuggable = false
            //signingConfig = signingConfigs.getByName("production")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("String", "SERVER_URL", "\"${project.properties["dev_server_url"]}\"")
            buildConfigField("String", "UTILITY_SERVER_URL", "\"${project.properties["dev_utility_server_url"]}\"")
            buildConfigField("String", "ANALYTICS_SERVER_URL", "\"${project.properties["dev_analytics_server_url"]}\"")
            buildConfigField("String", "SERVICE_SERVER_URL", "\"${project.properties["dev_service_server_url"]}\"")
            buildConfigField("String", "FLURRY_API_KEY", "\"${project.properties["dev_flurry_api_key"]}\"")
            buildConfigField("String", "NATS_SERVER_1", "\"${project.properties["dev_nats_server_1"]}\"")
            buildConfigField("String", "NATS_SERVER_2", "\"${project.properties["dev_nats_server_2"]}\"")
            buildConfigField("String", "NATS_SERVER_3", "\"${project.properties["dev_nats_server_3"]}\"")
            buildConfigField("String", "NATS_ID", "\"${project.properties["dev_nats_id"]}\"")
            buildConfigField("String", "NATS_PASS", "\"${project.properties["dev_nats_pass"]}\"")
            buildConfigField("String", "POSTHOG_API_KEY", "\"${project.properties["posthog_api_key"]}\"")
            buildConfigField("String", "POSTHOG_HOST", "\"${project.properties["posthog_host"]}\"")
            buildConfigField("String", "PostHogProjectName", "\"Launcher_Generic_Carousel_\"")
            isDebuggable = true
            versionNameSuffix = "-dev"
            //signingConfig = signingConfigs.getByName("development")
        }
    }

    /*flavorDimensions += "customer"
    productFlavors {
        create("generic") {
            dimension = "customer"
            applicationIdSuffix = ".com"
            versionNameSuffix = "-gen-com"
        }
        create("cvte") {
            dimension = "customer"
            applicationIdSuffix = ".com"
            versionNameSuffix = "-cvte-com"
        }
        create("toptech") {
            dimension = "customer"
            applicationIdSuffix = ".com"
            versionNameSuffix = "-tt-com"
        }
        create("kitking") {
            dimension = "customer"
            applicationIdSuffix = ".com"
            versionNameSuffix = "-kk-com"
        }
        create("videoscape") {
            dimension = "customer"
            applicationIdSuffix = ".com"
            versionNameSuffix = "-vs-com"
        }
    }*/

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    //implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.leanback)
    //implementation(libs.glide)
    implementation("com.github.bumptech.glide:glide:4.13.0")
    //annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")
    kapt("com.github.bumptech.glide:compiler:4.13.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // timber
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.squareup:otto:1.3.8")

    //implementation("ua.com.crosp.solutions.library:pretty-toast:0.2.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.google.zxing:core:3.3.3")
    //implementation("com.spoton:nats-android:2.4.6")

    // FOR POSTHOG
    //implementation("com.posthog:posthog-android:3.4.2")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    implementation("com.squareup.moshi:moshi-adapters:1.11.0")

    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")


    val media3_version = "1.4.1"
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    implementation("androidx.media3:media3-ui:$media3_version")
    implementation("androidx.media3:media3-exoplayer-ima:$media3_version")
    implementation("androidx.multidex:multidex:2.0.1")
}