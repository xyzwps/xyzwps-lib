pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "xyzwps-lib"

// https://docs.gradle.org/current/userguide/platforms.html
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("avaje", "9.12")
            library("avaje-inject", "io.avaje", "avaje-inject").versionRef("avaje")
            library("avaje-injectgen", "io.avaje", "avaje-inject-generator").versionRef("avaje")

            library("h2", "com.h2database:h2:2.2.224")
            library("hikari", "com.zaxxer:HikariCP:5.1.0")
            library("mysql", "com.mysql:mysql-connector-j:8.4.0")

            library("testcontainers-bom", "org.testcontainers:testcontainers-bom:1.19.8")

            library("hocon", "com.typesafe:config:1.4.3")

            library("slf4j-api", "org.slf4j:slf4j-api:2.0.13")
            library("jboss-logging", "org.jboss.logging:jboss-logging:3.6.0.Final")
            library("logback-classic", "ch.qos.logback:logback-classic:1.4.6")
            bundle("logging", listOf("jboss-logging", "slf4j-api", "logback-classic"))

            version("junit", "5.10.2")
            library("junit-bom", "org.junit", "junit-bom").versionRef("junit")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")

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
    "xyzwps-express:server-helidon"
)
include("xyzwps-jdbc")
include("xyzwps-json")
include("xyzwps-website")
include("lib:jsdom:mimetype")
include("lib:jshttp:mime-db")
