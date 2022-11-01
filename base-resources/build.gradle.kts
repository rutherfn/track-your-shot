plugins {
    id(BuildIds.androidLibrary)
}

android {
    defaultConfig {
        minSdk = ConfigurationData.minSdk
    }

    compileSdk = ConfigurationData.compileSdk
}

dependencies {}