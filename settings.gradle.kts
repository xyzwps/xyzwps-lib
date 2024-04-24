rootProject.name = "xyzwps-lib"

// https://docs.gradle.org/current/userguide/platforms.html
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("dagger", "2.51.1")
            library("dagger", "com.google.dagger", "dagger").versionRef("dagger")
            library("dagger-compiler", "com.google.dagger", "dagger-compiler").versionRef("dagger")

            version("jackson", "2.17.0")
            library("jackson-core", "com.fasterxml.jackson.core", "jackson-core").versionRef("jackson")
            library("jackson-databind", "com.fasterxml.jackson.core", "jackson-databind").versionRef("jackson")
            bundle("jackson", listOf("jackson-core", "jackson-databind"))

            version("junit", "5.10.2")
            library("junit-bom", "org.junit", "junit-bom").versionRef("junit")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
        }
    }
}


include("xyzwps-bean")
include("xyzwps-bedrock")
include("xyzwps-collection")
include("xyzwps-dollar")
include("xyzwps-express")
include("xyzwps-json")
include("xyzwps-website")
include("lib:jshttp:mime-db")
