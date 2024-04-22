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
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0") // TODO: api 不能用了，得想个办法
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.google.dagger:dagger:2.51.1")
    annotationProcessor("com.google.dagger:dagger-compiler:2.51.1")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
