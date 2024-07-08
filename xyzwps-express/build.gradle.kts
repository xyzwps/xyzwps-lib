plugins {
    java
    jacoco
    `java-library`
}

group = "com.xyzwps.lib.express"
version = findProperty("lib.version")!!
// TODO: 使用 properties 文件的写法太啰嗦了，想办法改成插件
java.sourceCompatibility = JavaVersion.valueOf("VERSION_" + findProperty("lib.java.version"))

repositories {
    mavenCentral()
}

dependencies {
    api(project(":xyzwps-http"))
    api(project(":xyzwps-bedrock"))
    api(project(":xyzwps-dollar"))
    api(project(":xyzwps-json"))
    api(libs.bundles.logging)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}
