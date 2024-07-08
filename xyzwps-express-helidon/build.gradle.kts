plugins {
    java
    jacoco
    `java-library`
}

group = "com.xyzwps.lib.express"
version = findProperty("lib.version")!!

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":xyzwps-express"))
    implementation(project(":xyzwps-express-commons"))
    implementation("io.helidon.webserver:helidon-webserver:4.0.10")

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}
