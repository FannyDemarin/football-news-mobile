import java.util.Properties
import java.io.FileInputStream
import java.util.Base64

// Charger les propriétés du keystore
val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("key.properties")
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

plugins {
    id("com.android.application")
    id("kotlin-android")
    // Le plugin Flutter doit être appliqué après Android et Kotlin
    id("dev.flutter.flutter-gradle-plugin")
}

val keystoreBase64 = System.getenv("KEY_JKS")
val keystorePassword = System.getenv("KEY_PASSWORD")

val keystoreFile = File("release.keystore")

if (!keystoreFile.exists()) {
    keystoreFile.writeBytes(Base64.getDecoder().decode(keystoreBase64))
}

android {
    namespace = "com.example.football_news"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        applicationId = "com.example.football_news"
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    signingConfigs {
        create("release") {
            keyAlias =  "upload"
            keyPassword = keystorePassword
            storeFile = keystoreFile
            storePassword = keystorePassword 
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

flutter {
    source = "../.."
}
