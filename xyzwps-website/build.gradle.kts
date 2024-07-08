plugins {
    java
}

group = "com.xyzwps.lib"
version = findProperty("lib.version")!!
java.sourceCompatibility = JavaVersion.valueOf("VERSION_" + findProperty("lib.java.version"))

repositories {
    mavenCentral()
}

object Versions {
    const val AVAJE_INJECT = "9.12"
    const val AVAJE_VALIDATOR = "1.5"
}

dependencies {
    implementation(project(":xyzwps-express"))
    implementation(project(":xyzwps-express-bio"))
    implementation(project(":xyzwps-jdbc"))
    implementation(libs.hikari)
    implementation(libs.mysql)
    implementation(libs.hocon)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation("io.avaje:avaje-inject:${Versions.AVAJE_INJECT}")
    annotationProcessor("io.avaje:avaje-inject-generator:${Versions.AVAJE_INJECT}")
    implementation("io.avaje:avaje-validator:${Versions.AVAJE_VALIDATOR}")
    implementation("io.avaje:avaje-validator-constraints:${Versions.AVAJE_VALIDATOR}")
    annotationProcessor("io.avaje:avaje-validator-generator:${Versions.AVAJE_VALIDATOR}")

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation("io.avaje:avaje-inject:${Versions.AVAJE_INJECT}")
    testAnnotationProcessor("io.avaje:avaje-inject-generator:${Versions.AVAJE_INJECT}")
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