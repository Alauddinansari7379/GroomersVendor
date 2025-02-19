plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")


}

android {
    namespace = "com.groomers.groomersvendor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.groomers.groomersvendor"
        minSdk = 21
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
    buildFeatures{
        viewBinding = true
    }
    buildFeatures{
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Counter Code Picker
    implementation ("com.hbb20:ccp:2.5.0")
    implementation ("com.github.ibrahimsn98:SmoothBottomBar:1.7.8")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.2")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation ("androidx.core:core:1.10.1")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    //okhttp3
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    //viewmodel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    //
    implementation ("com.intuit.sdp:sdp-android:1.0.6")
    //loader
    implementation ("com.airbnb.android:lottie:6.1.0")
    //cardview
    implementation ("androidx.cardview:cardview:1.0.0")
    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //SweetAlert
    implementation ("com.github.f0ris.sweetalert:library:1.6.2")
    //dependency injection
    implementation ("com.google.dagger:hilt-android:2.50")
    kapt ("com.google.dagger:hilt-compiler:2.50")

    implementation ("com.squareup.picasso:picasso:2.8")   //
//    implementation(project(":library"))

}