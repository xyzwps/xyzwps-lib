plugins {
    java
}

group = "com.xyzwps.lib"
version = findProperty("lib.version")!!
// TODO: 使用 properties 文件的写法太啰嗦了，想办法改成插件
java.sourceCompatibility = JavaVersion.valueOf("VERSION_" + findProperty("lib.java.version"))

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":xyzwps-express"))
    implementation(project(":xyzwps-express:server-bio"))
    implementation(project(":xyzwps-express:server-undertow"))
    implementation(libs.hocon)
    implementation(libs.dagger)
    annotationProcessor(libs.dagger.compiler)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}


tasks.jar {
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.forEach {
        println("=> dependent jar: $it")
        copy {
            from(it)
            into("$projectDir/build/libs/dependencies")
        }
    }
}