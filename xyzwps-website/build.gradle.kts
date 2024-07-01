plugins {
    java
}

group = "com.xyzwps.lib"
version = findProperty("lib.version")!!
java.sourceCompatibility = JavaVersion.valueOf("VERSION_" + findProperty("lib.java.version"))

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":xyzwps-express"))
    implementation(project(":xyzwps-express:server-bio"))
    implementation(project(":xyzwps-jdbc"))
    implementation(libs.hikari)
    implementation(libs.mysql)
    implementation(libs.hocon)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation(libs.avaje.inject)
    annotationProcessor(libs.avaje.inject.generator)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.avaje.inject)
    testAnnotationProcessor(libs.avaje.inject.generator)
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