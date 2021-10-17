plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

dependencies {
    api("net.mamoe", "mirai-core", "2.8.0-M1")
    implementation("io.ktor:ktor-client-core:1.6.3")
    implementation("io.ktor:ktor-client-cio:1.6.3")
    implementation("com.fasterxml.jackson.core","jackson-core","2.12.1")
    implementation("com.fasterxml.jackson.core","jackson-annotations","2.12.1")
    implementation("com.fasterxml.jackson.core","jackson-databind","2.12.1")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(kotlin("stdlib"))
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    this.destinationDir = file("$rootDir/build/libs")
}