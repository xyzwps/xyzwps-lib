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
    implementation(project(":lib:jsdom:mimetype"))
    implementation(project(":lib:jshttp:mime-db"))
    implementation(project(":xyzwps-bedrock"))
    implementation(project(":xyzwps-dollar"))
    implementation(project(":xyzwps-express"))
    api(libs.slf4j.api)
    api(libs.slf4j.simple)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}
