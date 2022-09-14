package types

/**
 * To define build type data */
interface Build {
    var appName: String
    var applicationIdSuffix: String
    var buildName: String
    var isDebuggable: Boolean
    var isMinifyEnabled: Boolean
}
