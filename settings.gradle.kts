rootProject.name = "xyzwps-lib"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("dagger", "com.google.dagger:dagger:2.51.1")
            library("dagger-complier", "com.google.dagger:dagger-compiler:2.51.1")
            library("jackson-core", "com.fasterxml.jackson.core:jackson-core:2.17.0")
            library("jackson-databind", "com.fasterxml.jackson.core:jackson-databind:2.17.0")
            library("junit-bom", "org.junit:junit-bom:5.10.2")
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
