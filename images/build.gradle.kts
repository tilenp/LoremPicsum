plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.images"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {

    implementation(project(":core"))
    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    /** Coroutines **/
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    /** Coil **/
    implementation("io.coil-kt:coil-compose:2.2.2")

    /** Compose **/
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha03")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))

    /** DataStore **/
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    /** Hilt **/
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    /** JUnit 5 **/
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    /** MockK **/
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("io.mockk:mockk-agent-jvm:1.13.4")

    /** Turbine **/
    testImplementation("app.cash.turbine:turbine:0.11.0")

    /** ViewModel **/
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}

kapt {
    correctErrorTypes = true
}