plugins {
    java
    `java-library`
}

group = "com.xyzwps.lib"
version = findProperty("lib.version")!!
// TODO: 使用 properties 文件的写法太啰嗦了，想办法改成插件
java.sourceCompatibility = JavaVersion.valueOf("VERSION_" + findProperty("lib.java.version"))

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":xyzwps-bean"))
    implementation(project(":xyzwps-bedrock"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

tasks.test {
    useJUnitPlatform()
}
