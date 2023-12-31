plugins {
    id("com.android.application")
}

android {
    namespace = "com.dylanlong.fridgeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dylanlong.fridgeapp"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val fragment_version = "1.6.1"
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // https://github.com/journeyapps/zxing-android-embedded
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    implementation("androidx.fragment:fragment:$fragment_version")

    //add below dependancy for using room.
    implementation("androidx.room:room-common:2.6.0")
    implementation("androidx.room:room-runtime:2.6.0")

    //add below dependancy for using lifecycle extensions for room.
    annotationProcessor("androidx.room:room-compiler:2.6.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}