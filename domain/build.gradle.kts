plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.domain"
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
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {

    implementation(project(":core"))
    implementation(project(":data"))

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
}

kapt {
    correctErrorTypes = true
}