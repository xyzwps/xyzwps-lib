rootProject.name = "xyzwps-lib"

// https://docs.gradle.org/current/userguide/platforms.html
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("dagger", "2.51.1")
            library("dagger", "com.google.dagger", "dagger").versionRef("dagger")
            library("dagger-compiler", "com.google.dagger", "dagger-compiler").versionRef("dagger")

            library("h2", "com.h2database:h2:2.2.224")

            library("hocon", "com.typesafe:config:1.4.3")

            version("jackson", "2.17.0")
            library("jackson-core", "com.fasterxml.jackson.core", "jackson-core").versionRef("jackson")
            library("jackson-databind", "com.fasterxml.jackson.core", "jackson-databind").versionRef("jackson")
            bundle("jackson", listOf("jackson-core", "jackson-databind"))

            version("junit", "5.10.2")
            library("junit-bom", "org.junit", "junit-bom").versionRef("junit")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")

            version("slf4j", "2.0.13")
            library("slf4j-api", "org.slf4j", "slf4j-api").versionRef("slf4j")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            bundle("slf4j", listOf("slf4j-api", "slf4j-simple"))

            version("undertow", "2.3.13.Final")
            library("undertow-core", "io.undertow", "undertow-core").versionRef("undertow")

            version("lombok", "1.18.32")
            library("lombok", "org.projectlombok", "lombok").versionRef("lombok")
        }
    }
}

include("doc")
include("xyzwps-bean")
include("xyzwps-bedrock")
include(
    "xyzwps-dollar", //
    "xyzwps-dollar:api",
    "xyzwps-dollar:generator",
    "xyzwps-dollar:iterator",
    "xyzwps-dollar:performance",
    "xyzwps-dollar:seq",
    "xyzwps-dollar:test-cases"
)
include(
    "xyzwps-express",  //
    "xyzwps-express:server-bio",
    "xyzwps-express:server-nio",
    "xyzwps-express:server-commons",
    "xyzwps-express:server-undertow"
)
include("xyzwps-jdbc")
include("xyzwps-json")
include("xyzwps-log")
include("xyzwps-website")
include("lib:jsdom:mimetype")
include("lib:jshttp:mime-db")
