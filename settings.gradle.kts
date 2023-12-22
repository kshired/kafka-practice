rootProject.name = "kafka"

include(
    // "producers",
    // "consumers",
    // "practice"
)

pluginManagement {
    val kotlinVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
            }
        }
    }
}
