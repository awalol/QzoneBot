plugins {
    java
    kotlin("jvm") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "cn.awalol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("net.mamoe", "mirai-core", "2.6.2")
    implementation("org.seleniumhq.selenium","selenium-java","4.0.0-beta-3")
    implementation("org.apache.httpcomponents","httpclient","4.5.13")
    implementation("com.fasterxml.jackson.core","jackson-core","2.12.1")
    implementation("com.fasterxml.jackson.core","jackson-annotations","2.12.1")
    implementation("com.fasterxml.jackson.core","jackson-databind","2.12.1")

    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "cn.awalol.qzoneBot.MainKt"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
    kotlinOptions.jvmTarget = "1.8"
}