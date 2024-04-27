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
    api(libs.undertow.core)
    implementation(project(":lib:jsdom:mimetype"))
    implementation(project(":xyzwps-bedrock"))
    implementation(project(":xyzwps-express"))

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}
