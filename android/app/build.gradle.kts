// top-level imports required in Kotlin DSL
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    // START: FlutterFire Configuration
    id("com.google.gms.google-services")
    // END: FlutterFire Configuration
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

// Custom function to safely load properties from key.properties or fall back to environment variables (CI/CD)
fun getProperty(key: String): String? {
    val properties = Properties()
    val keystorePropertiesFile = rootProject.file("key.properties")

    // Check key.properties first (for local development)
    if (keystorePropertiesFile.exists()) {
        FileInputStream(keystorePropertiesFile).use { properties.load(it) }
        return properties[key] as? String
    }

    // Fallback to environment variable (for CI/CD)
    return System.getenv(key)
}

android {
    namespace = "com.example.anime_verse"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = "29.0.13113456"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    signingConfigs {
        create("release") {
            // Using environment variables (passed by GitHub Actions) or local key.properties
            keyAlias = getProperty("KEY_ALIAS")
            keyPassword = getProperty("KEY_PASSWORD")
            // STORE_FILE path is set in the YAML action
            storeFile = getProperty("STORE_FILE")?.let { file(it) }
            storePassword = getProperty("STORE_PASSWORD")
        }
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.example.anime_verse"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            // Ensure the signing config is set to the 'release' config we just defined
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

flutter {
    source = "../.."
}