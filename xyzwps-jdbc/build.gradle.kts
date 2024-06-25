plugins {
    java
    `java-library`
}

group = "com.xyzwps.lib"
version = findProperty("lib.version")!!
java.sourceCompatibility = JavaVersion.valueOf("VERSION_" + findProperty("lib.java.version"))

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":xyzwps-bedrock"))
    implementation(project(":xyzwps-bean"))
    implementation(project(":xyzwps-dollar"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.h2)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

tasks.test {
    useJUnitPlatform()
}
