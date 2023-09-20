plugins {
    java
    groovy
}

group = "com.xyzwps.lib"
version = findProperty("lib.version")!!

java.sourceCompatibility = JavaVersion.valueOf("VERSION_" + findProperty("lib.java.version"))

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.apache.groovy:groovy:4.0.13")
}

tasks.test {
    useJUnitPlatform()
}