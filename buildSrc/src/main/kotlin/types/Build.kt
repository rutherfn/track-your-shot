package types

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * To define build type data */
interface Build {
    var appName: String
    var applicationIdSuffix: String
    var buildName: String
    var isDebuggable: Boolean
    var isMinifyEnabled: Boolean
    var appIconRoute: String
    var roundAppIconRoute: String
}
