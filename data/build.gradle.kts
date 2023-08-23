plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
}

android {
    namespace = "com.example.data"
    compileSdk = 33

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
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {

    implementation(project(":core"))

    /** Coroutines **/
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    /** Hilt **/
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    /** JUnit 5 **/
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    /** MockK **/
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("io.mockk:mockk-agent-jvm:1.13.4")

    /** Mock web server **/
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")

    /** OkHttp **/
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    /** Retrofit **/
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    /** Room **/
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
}

kapt {
    correctErrorTypes = true
}