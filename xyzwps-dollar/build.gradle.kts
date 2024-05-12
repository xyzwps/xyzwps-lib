plugins {
    jacoco
    java
    `java-library`
    `maven-publish`
    id("me.champeau.jmh") version "0.7.1"
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
    api(project(":xyzwps-dollar:api"))
    implementation(project(":xyzwps-dollar:seq"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
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

jmh {
    warmupIterations.set(1)
    iterations.set(2)
    fork.set(2)
}
