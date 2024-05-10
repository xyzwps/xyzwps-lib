plugins {
    jacoco
    java
    `java-library`
    `maven-publish`
}

group = "com.xyzwps.lib"
version = findProperty("lib.version")!!

java.sourceCompatibility = JavaVersion.valueOf("VERSION_" + findProperty("lib.java.version"))

repositories {
    maven {
        setUrl("https://maven.aliyun.com/repository/public/")
    }
    mavenCentral()
}

dependencies {
    implementation(project(":xyzwps-dollar:api"))
    implementation(platform(libs.junit.bom))
    implementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

java {
    withJavadocJar()
    withSourcesJar()
}
